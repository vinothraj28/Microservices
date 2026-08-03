package com.microservices.gateway.services.gRPCServices;

import com.microservices.gateway.DTOS.movie.MovieImageResponseDTO;
import com.microservices.gateway.excpetions.ImageException;
import com.microservices.movie.grpc.GetImageRequest;
import com.microservices.movie.grpc.GetImageResponse;
import com.microservices.movie.grpc.ImageServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ImageGrpcService {

    @GrpcClient("image-service")
    private ImageServiceGrpc.ImageServiceBlockingStub imageServiceBlockingStub;

    public MovieImageResponseDTO getImageById(Long imageId) {

        log.info("Requesting image with id: {} from Image Service via gRPC", imageId);

        try {
            GetImageRequest request = GetImageRequest.newBuilder()
                    .setImageId(imageId)
                    .build();

            GetImageResponse response = imageServiceBlockingStub.getImage(request);

            return new MovieImageResponseDTO(
                    response.getData().toByteArray(),
                    response.getFileName(),
                    response.getContentType(),
                    response.getSize()
            );
        } catch (StatusRuntimeException ex) {
            log.error("Error occurred while fetching image with id: {}. Error: {}", imageId, ex.getMessage());
            throw new ImageException("Failed to fetch image with id: " + imageId + ". Error: " + ex.getMessage());
        }
    }

}
