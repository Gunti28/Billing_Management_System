package com.srnrit.BMS.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.srnrit.BMS.dao.ProductDao;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.productexceptions.ProductNotCreatedException;
import com.srnrit.BMS.mapper.DTOToEntity;
import com.srnrit.BMS.mapper.EntityToDTO;
import com.srnrit.BMS.service.IProductService;

@Service
public class ProductServiceImpl implements IProductService {
	
	private ProductDao productDao;
	
	public ProductServiceImpl(ProductDao productDao) {
		super();
		this.productDao = productDao;
	}

	@Override
	public ProductResponseDTO storeProduct(ProductRequestDTO productRequestDTO) {
		if(productRequestDTO != null) {
			Product product = DTOToEntity.toProduct(productRequestDTO);
			Optional<Product> productStored = this.productDao.saveProduct(product, productRequestDTO.getCategoryId());
			return EntityToDTO.toProductResponseDTO(
						productStored.orElseThrow(() -> new ProductNotCreatedException("Product not created")));
		
		}else {
			throw new RuntimeException("Invalid JSON !");
		}
	}

	@Override
	public ProductResponseDTO getProductByProductName(String productName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteProductByProductId(String productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductResponseDTO updateProductByProductId(ProductRequestDTO productRequestDTO, String productId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProductResponseDTO fetchProductByAvailability(ProductRequestDTO productRequestDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ProductResponseDTO> getAllProducts() {
		// TODO Auto-generated method stub
		return null;
	}

}
