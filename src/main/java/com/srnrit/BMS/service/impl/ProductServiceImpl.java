package com.srnrit.BMS.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.srnrit.BMS.dao.ProductDao;
import com.srnrit.BMS.dto.ProductRequestDTO;
import com.srnrit.BMS.dto.ProductResponseDTO;
import com.srnrit.BMS.entity.Product;
import com.srnrit.BMS.exception.productexceptions.ProductNotCreatedException;
import com.srnrit.BMS.exception.productexceptions.ProductNotFoundException;
import com.srnrit.BMS.exception.productexceptions.UnsupportedFileTypeException;
import com.srnrit.BMS.mapper.DTOToEntity;
import com.srnrit.BMS.mapper.EntityToDTO;
import com.srnrit.BMS.service.IProductService;
import com.srnrit.BMS.util.FileStorageProperties;
import com.srnrit.BMS.util.idgenerator.ProductImageFileNameGenerator;

@Service
public class ProductServiceImpl implements IProductService {

	@Autowired
	private ProductDao productDao;
	private FileStorageProperties fileStorageProperties;

	public ProductServiceImpl(ProductDao productDao, FileStorageProperties fileStorageProperties) {
		this.productDao = productDao;
		this.fileStorageProperties = fileStorageProperties;
	}

	/**
	 * Store a new product along with an image file.
	 */
	@Override
	public ProductResponseDTO storeProduct(ProductRequestDTO productRequestDTO,String categoryId,MultipartFile file) {
		if (productRequestDTO != null) 
		{
			//step-1
			//converting productRequestDTO to product entity
			Product product = DTOToEntity.toProduct(productRequestDTO);
			
			//step-2
			//validate that product image
			
			boolean isValidImage = this.validateProductImage(file);
			
			if(isValidImage)
			{
				//step-1
				//get new productImagename
				String productImageName = ProductImageFileNameGenerator.getNewFileName(file.getOriginalFilename());
				
				//add this productImageName into product
				product.setProductImage(productImageName);
				
				//store the product in db
				
				Optional<Product> savedProduct = this.productDao.saveProduct(product, categoryId);
			
			   //store that product image in local drive if it stored in db
				
				if(savedProduct.isPresent())
				{
					boolean storeImage = this.productDao.storeImage(file, productImageName);
					
					if(storeImage)
					{
						System.out.println("Image stored successfully");
						//finally create productResponseDTO 
						ProductResponseDTO productResponseDTO = new ProductResponseDTO();
						
						productResponseDTO=EntityToDTO.toProductResponseDTO(savedProduct.get());
						
						return productResponseDTO;
					}
					else throw new RuntimeException("Something went wrong during storing image");
				}
				else throw new ProductNotCreatedException("Product not stored ! try after sometime");
			}
			else throw new RuntimeException("something went wrong");
		}
		else throw new RuntimeException("ProductRequestDTO must not be null");
	}
			

	/**
	 * Fetch product by name.
	 */
	@Override
	public ProductResponseDTO getProductByProductName(String productName) {
		Optional<List<Product>> products = this.productDao.searchProductByName(productName);
		return products.flatMap(list -> list.stream().findFirst().map(EntityToDTO::toProductResponseDTO))
				.orElseThrow(() -> new ProductNotFoundException("No product found with name: " + productName));
	}

	/**
	 * Delete product by ID.
	 */
	@Override
	public String deleteProductByProductId(String productId) {
		Optional<String> result = this.productDao.deleteProductById(productId);
		return result.orElseThrow(
				() -> new ProductNotFoundException("Product not found for deletion with ID: " + productId));
	}

	/**
	 * Update product by ID.
	 */
	@Override
	public ProductResponseDTO updateProductByProductId(ProductRequestDTO productRequestDTO, String productId) {
		Product product = DTOToEntity.toProduct(productRequestDTO);
		product.setProductId(productId);

		Optional<Product> updatedProduct = this.productDao.updateProduct(product);
		return EntityToDTO.toProductResponseDTO(updatedProduct
				.orElseThrow(() -> new ProductNotFoundException("Failed to update product with ID: " + productId)));
	}

	/**
	 * Fetch all products.
	 */
	@Override
	public List<ProductResponseDTO> getAllProducts() {
		Optional<List<Product>> products = this.productDao.fetchAllProduct();
		return products.orElseThrow(() -> new RuntimeException("No products available!")).stream()
				.map(EntityToDTO::toProductResponseDTO).collect(Collectors.toList());
	}

	/**
	 * Fetch products by availability status.
	 */
	@Override
	public List<ProductResponseDTO> fetchProductByAvailability(Boolean inStock) {
		Optional<List<Product>> products = this.productDao.fetchProductByAvailability(inStock);
		return products.orElseThrow(() -> new RuntimeException("No products found with specified availability!"))
				.stream().map(EntityToDTO::toProductResponseDTO).collect(Collectors.toList());
	}

	

	private String getFileExtension(String originalFilename) {
		int dotIndex = originalFilename.lastIndexOf(".");
		if (dotIndex == -1)
			throw new UnsupportedFileTypeException("Invalid File");
		return originalFilename.substring(dotIndex + 1);

	}
	
	
	private boolean validateProductImage(MultipartFile file)
	{
		
		if (file != null) 
		{
			long maxSize = fileStorageProperties.getGetMaxFileSize();
			if (file.getSize() <= maxSize) 
			{
				String contentType = file.getContentType();
				if (contentType.startsWith("image/"))
				{
					// jpg,png,
					String fileNameExtension = getFileExtension(file.getOriginalFilename());
					if (Arrays.asList("jpg", "jpeg", "png", "git", "tiff", "bmp", "svg", "webp", "heif")
							.contains(fileNameExtension.toLowerCase()))
					{
						return true;
					}
					return false;
				}
				else throw new UnsupportedFileTypeException("Invalid File Type ! Only images are allowed");
			     
			}
			else throw new RuntimeException("File size exceeds maximum limit! Supported file size " + maxSize);
		}
		else throw new RuntimeException("image can't be null");
		
		
	}

	
	
	
}
