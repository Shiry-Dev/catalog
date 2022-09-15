package com.shirydev.dscatalog.services;

import com.shirydev.dscatalog.dto.CategoryDTO;
import com.shirydev.dscatalog.entities.Category;
import com.shirydev.dscatalog.repositories.CategoryRepository;
import com.shirydev.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;


    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        List<Category> list = repository.findAll();

//        List<CategoryDTO> listDto = new ArrayList<>();
//        for(Category cat: list){
//            listDto.add(new CategoryDTO(cat));
//        }
//        return listDto;

        List<CategoryDTO> listDto = list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
        return listDto;
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));
        return new CategoryDTO(entity);
    }
}
