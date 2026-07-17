package com.microservices.profile.services.Impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.microservices.profile.services.QrCodeService;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

@Service
public class QrCodeServiceIml implements QrCodeService {

    @Override
    public String generateQrCode(String text) {

        try {
            int width = 300;
            int height = 300;

            BitMatrix matrix =
                    new MultiFormatWriter()
                            .encode(
                                    text,
                                    BarcodeFormat.QR_CODE,
                                    width,
                                    height
                            );

            BufferedImage image =
                    MatrixToImageWriter.toBufferedImage(matrix);

            ByteArrayOutputStream output =
                    new ByteArrayOutputStream();

            ImageIO.write(
                    image,
                    "png",
                    output
            );

            byte[] bytes = output.toByteArray();

            return Base64.getEncoder()
                    .encodeToString(bytes);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed generating QR code",
                    e
            );
        }
    }
}
