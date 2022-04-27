package com.romanyuta.note.controller;

import com.romanyuta.note.model.Record;
import com.romanyuta.note.model.RecordRequest;
import com.romanyuta.note.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RecordController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @RequestMapping(value = {"/recordList"},method = RequestMethod.GET)
    public String recordList(Model model){
        Iterable<Record> records = recordService.getAllRecords();
        model.addAttribute("records",records);

        return "recordList";
    }

    @RequestMapping(value = {"/recordInfo/{id}"},method = RequestMethod.GET)
    public String recordInfo(Model model, @PathVariable Long id){
        try {
            Record record = recordService.getRecord(id);
            model.addAttribute("record",record);
        }catch (Exception ex){
            model.addAttribute("errorMessage","Record not found");
        }
        return "recordInfo";
    }

    @RequestMapping(value = {"/addRecord"},method = RequestMethod.GET)
    public String showAddRecordPage(Model model){

        RecordRequest recordRequest = new RecordRequest();
        model.addAttribute("recordRequest",recordRequest);

        return "addRecord";
    }


    @RequestMapping(value = {"/addRecord"},method = RequestMethod.POST)
    public String saveRecord(Model model, @ModelAttribute("recordRequest") RecordRequest recordRequest){

        String title = recordRequest.getTitle();
        String text = recordRequest.getText();
        String author = recordRequest.getAuthor();

        try {
            if(title != null && text != null && author != null){
                recordService.addRecord(recordRequest);
            }
            return "redirect:/recordList";
        }catch (Exception ex){
            model.addAttribute("errorMessage","errorMessage");
            return "addRecord";
        }
    }

    @RequestMapping(value = {"/recordInfo/{id}/edit"}, method = RequestMethod.GET)
    public String showEditRecord(Model model, @PathVariable Long id){
        try{
            Record record = recordService.getRecord(id);
            RecordRequest request = new RecordRequest(record.getTitle(),record.getText(),record.getAuthor());
            model.addAttribute("recordRequest",request);
            model.addAttribute("record_id",id);
        }catch (Exception ex){
            model.addAttribute("errorMessage","Record not found");
        }
        return "editRecord";
    }

    @RequestMapping(value = {"/recordInfo/{id}/edit"},method = RequestMethod.POST)
    public String updateRecord(Model model, @PathVariable Long id, @ModelAttribute("recordRequest") RecordRequest request){
        try {
            recordService.updateRecord(request.getTitle(),request.getAuthor(),request.getText(),id);
            return "redirect:/recordInfo/"+id;
        }catch (Exception ex){
            model.addAttribute("errorMessage","errorMessage");
            return "editRecord";
        }
    }

    @RequestMapping(value = {"recordInfo/{id}/delete"},method = RequestMethod.GET)
    public String showDeleteRecordById(Model model,@PathVariable Long id){
        try {
            Record record = recordService.getRecord(id);
            model.addAttribute("allowDelete",true);
            model.addAttribute("record",record);
        }catch (Exception ex){
            model.addAttribute("errorMessage","Planet not fount");
        }
        return "recordInfo";
    }

    @RequestMapping(value = {"recordInfo/{id}/delete"},method = RequestMethod.POST)
    public String deleteRecordById(Model model, @PathVariable Long id){
        try {
            recordService.deleteRecord(id);
            return "redirect:/recordList";
        }catch (Exception ex){
            model.addAttribute("errorMessage","errorMessage");
            return "recordInfo";
        }
    }

}


