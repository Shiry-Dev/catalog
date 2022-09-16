package com.shirydev.dscatalog.services;

import com.shirydev.dscatalog.dto.CategoryDTO;
import com.shirydev.dscatalog.entities.Category;
import com.shirydev.dscatalog.repositories.CategoryRepository;
import com.shirydev.dscatalog.resources.exceptions.DataBaseException;
import com.shirydev.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;


    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest){
        Page<Category> list = repository.findAll(pageRequest);

//        List<CategoryDTO> listDto = new ArrayList<>();
//        for(Category cat: list){
//            listDto.add(new CategoryDTO(cat));
//        }
//        return listDto;

        Page<CategoryDTO> listDto = list.map(x -> new CategoryDTO(x));
        return listDto;
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO dto){
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto){
        try {
            Category entity = repository.getReferenceById(id);
            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new CategoryDTO(entity);
        }catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("ID not found" + id);
        }
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("ID not found" + id);
        }catch (DataIntegrityViolationException e){
            throw new DataBaseException("Integrity violation");
        }
    }
}
