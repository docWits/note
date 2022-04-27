package com.romanyuta.note.service;

import com.romanyuta.note.model.Record;
import com.romanyuta.note.model.RecordRequest;
import com.romanyuta.note.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecordService {

    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    public List<Record> getAllRecords(){
        return recordRepository.findAll();
    }

    public Record getRecord(Long id){
        return recordRepository.getById(id);
    }

    public void addRecord(RecordRequest recordRequest){
        if (recordRequest != null){
            recordRepository.save(mapRecord(recordRequest));
        }
    }

    public void updateRecord(String title,String author,String text,Long id){
        if (recordRepository.existsById(id)){
            Record record = recordRepository.getById(id);
            record.setTitle(title);
            record.setText(text);
            record.setAuthor(author);
            recordRepository.saveAndFlush(record);
        }
    }

    public void deleteRecord(Long id){
        if(recordRepository.existsById(id)){
            recordRepository.deleteById(id);
        }
    }

    public static Record mapRecord (RecordRequest recordRequest){

        Record record = new Record();

        record.setTitle(recordRequest.getTitle());
        record.setText(recordRequest.getText());
        record.setAuthor(recordRequest.getAuthor());
        record.setCreatedDate(LocalDateTime.now());

        return record;
    }
}
