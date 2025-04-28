package com.train.ticket.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.train.ticket.constants.PathConstants;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Component
public class QRCodeGenerator {

    public static String generateQRCode(String text, int width, int height) throws IOException, WriterException {

        String directory = PathConstants.qrCodeDirectory;
        File dir = new File(directory);
        if(!dir.exists()){
            dir.mkdirs();
        }

        String fileName = "ticket_"+System.currentTimeMillis()+".png";
        String fullPath = directory+fileName;


        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width,height, hints);
        Path path = FileSystems.getDefault().getPath(fullPath);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

        File qrFile = new File(fullPath);
        if(!qrFile.exists()){
            throw new IOException("QR code file was not created : "+fullPath);
        }
        return fileName;
    }
}
