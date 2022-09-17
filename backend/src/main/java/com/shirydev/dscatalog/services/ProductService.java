package com.shirydev.dscatalog.services;

import com.shirydev.dscatalog.dto.ProductDTO;
import com.shirydev.dscatalog.entities.Product;
import com.shirydev.dscatalog.repositories.ProductRepository;
import com.shirydev.dscatalog.resources.exceptions.DataBaseException;
import com.shirydev.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;


    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        Page<Product> list = repository.findAll(pageRequest);

//        List<ProductDTO> listDto = new ArrayList<>();
//        for(Product cat: list){
//            listDto.add(new ProductDTO(cat));
//        }
//        return listDto;

        Page<ProductDTO> listDto = list.map(x -> new ProductDTO(x));
        return listDto;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id){
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto){
        Product entity = new Product();
//        entity.setName(dto.getName());
        entity = repository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto){
        try {
            Product entity = repository.getReferenceById(id);
//            entity.setName(dto.getName());
            entity = repository.save(entity);
            return new ProductDTO(entity);
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
