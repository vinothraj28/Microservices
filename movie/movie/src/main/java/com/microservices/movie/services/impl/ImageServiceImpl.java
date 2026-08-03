package com.microservices.movie.services.impl;

import com.microservices.movie.exceptions.ImageException;
import com.microservices.movie.models.entities.Image;
import com.microservices.movie.repositories.ImageRepository;
import com.microservices.movie.services.interfaces.ImageService;
import org.springframework.stereotype.Service;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public Image findByImageId(Long id) {
        return imageRepository.findById(id).orElseThrow(() -> new ImageException("Image not found with id: " + id));
    }
}
