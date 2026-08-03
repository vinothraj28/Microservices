package com.microservices.movie.services.interfaces;

import com.microservices.movie.models.entities.Image;

public interface ImageService {

    Image findByImageId(Long id);

}
