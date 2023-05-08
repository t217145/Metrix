package com.metrix.webportal.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@Controller
public class QRCodeController {
 
    @GetMapping("/qrcode/generate/{qrcode}")
    public @ResponseBody byte[] getQRCodeImage(@PathVariable("qrcode") String qrcode) throws WriterException, IOException {
        BitMatrix bitMatrix = new QRCodeWriter().encode(qrcode, BarcodeFormat.QR_CODE, 25, 25);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageConfig config = new MatrixToImageConfig( 0xFF000002 , 0xFFFFC041 ) ;

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", out, config);
        return out.toByteArray();
    }
}