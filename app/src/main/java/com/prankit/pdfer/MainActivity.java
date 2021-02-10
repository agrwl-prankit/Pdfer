package com.prankit.pdfer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private String[] infoArray = new String[]{"Name", "Company Name", "Address", "Phone", "Email"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView downloadBtn = findViewById(R.id.downloadBtn);
        downloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Downloading start", Toast.LENGTH_SHORT).show();
                createPdf();
            }
        });
    }

    private void createPdf() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            PdfDocument myPdfDocument = new PdfDocument();
            Paint myPaint = new Paint();

            PdfDocument.PageInfo myPageInfo1 = new PdfDocument.PageInfo
                    .Builder(250,400,1).create();
            PdfDocument.Page myPage1 = myPdfDocument.startPage(myPageInfo1);
            Canvas canvas = myPage1.getCanvas();

            // CREATE FORM IN PDF THROUGH CODE
            makeFormThroughCode(myPdfDocument, myPaint, myPageInfo1, myPage1, canvas);

            // write to storage
            writeToExternalStorage(myPdfDocument);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void writeToExternalStorage(PdfDocument myPdfDocument){
        File file = new File(Environment.getExternalStorageDirectory(), "/Firstpdf.pdf");
        try {
            myPdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        myPdfDocument.close();
        Log.i("dnl", file.getPath());
        Toast.makeText(this, "Downloaded successfully", Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    void makeFormThroughCode(PdfDocument myPdfDocument, Paint myPaint, PdfDocument.PageInfo myPageInfo1, PdfDocument.Page myPage1, Canvas canvas){
        myPaint.setTextAlign(Paint.Align.CENTER);
        myPaint.setTextSize(12f);
        canvas.drawText("AM Store", myPageInfo1.getPageWidth()/2, 30, myPaint);

        myPaint.setTextSize(6.0f);
        myPaint.setTextScaleX(1.5f);
        myPaint.setColor(Color.rgb(122, 119, 119));
        canvas.drawText("Main Market Ganjdundwara, Kasgnaj", myPageInfo1.getPageWidth()/2,40,myPaint);
        myPaint.setTextScaleX(1f);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(9.0f);
        myPaint.setColor(Color.rgb(122,119,119));
        canvas.drawText("Customer information: ",10,70,myPaint);

        myPaint.setTextAlign(Paint.Align.LEFT);
        myPaint.setTextSize(8.0f);
        myPaint.setColor(Color.BLACK);

        int startXPosition = 10;
        int startYPosition = 100;
        int endXPosition = myPageInfo1.getPageHeight() - 10;

        for (int i=0; i<5; i++){
            canvas.drawText(infoArray[i], startXPosition, startYPosition, myPaint);
            canvas.drawLine(startXPosition,startYPosition+3, endXPosition,startYPosition+3, myPaint);
            startYPosition += 20;
        }
        canvas.drawLine(80,92,80,190, myPaint);

        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setStrokeWidth(2);
        canvas.drawRect(10,200,myPageInfo1.getPageWidth()-10,300, myPaint);
        canvas.drawLine(85,200,85,300, myPaint);
        canvas.drawLine(163,200,163,300, myPaint);
        myPaint.setStrokeWidth(0);
        myPaint.setStyle(Paint.Style.FILL);

        canvas.drawText("Photo",35,250, myPaint);
        canvas.drawText("Photo",110,250, myPaint);
        canvas.drawText("Photo",190,250, myPaint);

        canvas.drawText("Notes",10,320, myPaint);
        canvas.drawLine(35,325, myPageInfo1.getPageWidth()-10,325, myPaint);
        canvas.drawLine(10,345, myPageInfo1.getPageWidth()-10,345, myPaint);
        canvas.drawLine(10,365, myPageInfo1.getPageWidth()-10,365, myPaint);

        myPdfDocument.finishPage(myPage1);
    }
}