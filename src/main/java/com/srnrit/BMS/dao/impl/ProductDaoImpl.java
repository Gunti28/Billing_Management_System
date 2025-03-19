package com.srnrit.BMS.dao.impl;

import java.util.List;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.srnrit.BMS.dao.ProductDao;
import com.srnrit.BMS.entity.Category;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.categoryexceptions.CategoryNotFoundException;
import com.srnrit.BMS.repository.CategoryRepository;
import com.srnrit.BMS.repository.ProductRepository;

@Repository
public class ProductDaoImpl implements ProductDao {

	private ProductRepository productRepository;
	private CategoryRepository categoryRepository;

	public ProductDaoImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
		super();
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	@Override
	public Optional<Product> saveProduct(Product product, String categoryId) {
		Boolean existsById = this.categoryRepository.existsById(categoryId);

		if (existsById) {
			Category category = this.categoryRepository.getReferenceById(categoryId);
			category.addProduct(product);
			product.setCategory(category);
			Product savedProduct = this.productRepository.save(product);
			return savedProduct != null ? Optional.of(savedProduct) : Optional.empty();
		} else {
			throw new CategoryNotFoundException("Category Not Found with Id:" + categoryId);
		}
	}
	
	@Override
	public Optional<List<Product>> fetchProductByAvailability(Boolean availability) {
	    List<Product> products = this.productRepository.findByInStock(availability);
	    return products.isEmpty() ? Optional.empty() : Optional.of(products);
	}



	@Override
	public Optional<String> deleteProductById(String productId) {
		Boolean existsById = this.productRepository.existsById(productId);
		if (existsById) {
			Product product = this.productRepository.getReferenceById(productId);
			product.getCategory().removeProduct(product);
			this.productRepository.delete(product);
			return Optional.of("Product successfully deleted with Id:" + productId);
		}
		return Optional.empty();
	}

	@Override
	public Optional<Product> updateProduct(Product product) {
		
		boolean existsById = this.productRepository.existsById(product.getProductId());
	    if(existsById)
	    {
	    	Product oldProduct = this.productRepository.getReferenceById(product.getProductId());
	        Category category = oldProduct.getCategory();
	        oldProduct.setInStock(product.getInStock());
	        oldProduct.setProductQuantity(product.getProductQuantity());
	        oldProduct.setProductImage(product.getProductImage());
	        oldProduct.setProductPrice(product.getProductPrice());
	        oldProduct.setProductName(product.getProductName());
	        Product updatedProduct = this.productRepository.save(oldProduct);
	        List<Product> products = category.getProducts();
	        boolean flag=false;
	        for(Product prod : products)
	        {
	        	if(prod.getProductId().equals(product.getProductId()))
	        	{
	        		
	        		prod.setProductImage(product.getProductImage());
	        		prod.setProductName(product.getProductName());
	        		prod.setProductPrice(product.getProductPrice());
	        		prod.setProductQuantity(product.getProductQuantity());
	        		prod.setInStock(product.getInStock());
	        		
	        		System.out.println("Category updated with product id : "+product.getProductId());
	        		flag=true;
	        		break;
	        	}
	        }
	        if(flag && updatedProduct!=null)
	        {
	        	return Optional.of(updatedProduct);
	        }
	        else
	        {
	        	return Optional.empty();
	        }
	        
	    }
	    else 
	    {
			return Optional.empty();
		}
	
	
	}	

	@Override
	public Optional<List<Product>> searchProductByName(String name) {
	    List<Product> products = this.productRepository.findByProductNameContainingIgnoreCase(name);
	    return products.isEmpty() ? Optional.empty() : Optional.of(products);
	}


	@Override
	public Optional<List<Product>> fetchAllProduct() {
		List<Product> products = this.productRepository.findAll();
		if (products != null && products.size() != 0) {
			return Optional.of(products);
		}
		return Optional.empty();
	}

}
