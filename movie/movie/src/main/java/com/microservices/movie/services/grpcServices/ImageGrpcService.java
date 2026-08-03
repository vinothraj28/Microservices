package com.microservices.movie.services.grpcServices;

import com.google.protobuf.ByteString;
import com.microservices.movie.grpc.GetImageRequest;
import com.microservices.movie.grpc.GetImageResponse;
import com.microservices.movie.grpc.ImageServiceGrpc;
import com.microservices.movie.models.entities.Image;
import com.microservices.movie.services.interfaces.ImageService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
public class ImageGrpcService extends ImageServiceGrpc.ImageServiceImplBase {

    private final ImageService imageService;


    public ImageGrpcService(ImageService imageService) {
        this.imageService = imageService;
    }

    @Override
    public void getImage(GetImageRequest getImageRequest,
                         StreamObserver<GetImageResponse> responseStreamObserver){

        log.info("Received gRPC request to get image with id: {}", getImageRequest.getImageId());

        Image image = imageService.findByImageId(getImageRequest.getImageId());

        log.info("Image found with id: {}, fileName: {}", image.getId(), image.getFileName());

        GetImageResponse getImageResponse = GetImageResponse.newBuilder()
                .setData(ByteString.copyFrom(image.getData()))
                .setSize(image.getSize().intValue())
                .setContentType(image.getContentType())
                .setFileName(image.getFileName())
                .build();

        responseStreamObserver.onNext(getImageResponse);
        responseStreamObserver.onCompleted();

    }
}
