package com.ugc.ch.createqrcode;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Button button,button1;
    EditText editText, editText1,editText2,editText3;
    public final static int QRcodeWidth = 500 ;
    Bitmap bitmap;
    String BOOKCODE,GRNNO,YYYYMM,SRNO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        imageView = (ImageView)findViewById(R.id.imageView);

        editText = (EditText)findViewById(R.id.editText);
        editText1 = (EditText)findViewById(R.id.editText1);
        editText2 = (EditText)findViewById(R.id.editText2);
        editText3 = (EditText)findViewById(R.id.editText3);

        button = (Button)findViewById(R.id.button);
        button1 = (Button)findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BOOKCODE = editText.getText().toString();
                GRNNO = editText1.getText().toString();
                YYYYMM = editText2.getText().toString();
                SRNO = editText3.getText().toString();
                try {
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("BOOKCODE", BOOKCODE.toString().trim());
                    jsonObj.put("GRNNO",GRNNO.toString().trim());
                    jsonObj.put("YYYYMM", YYYYMM.toString().trim());
                    jsonObj.put("SRNO",SRNO.toString().trim());

                    String ss = jsonObj.toString();

                    Bitmap bitmap = TextToImageEncode(ss);
                    imageView.setImageBitmap(bitmap);

                } catch (WriterException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialoglayout = inflater.inflate(R.layout.custom_dialog, null);
                ImageView imageView = (ImageView)dialoglayout.findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
                TextView txtName = (TextView)dialoglayout.findViewById(R.id.name);
                TextView txtAddress = (TextView)dialoglayout.findViewById(R.id.address);

                txtName.setText(BOOKCODE);
                txtAddress.setText(GRNNO);
                builder.setView(dialoglayout);
                builder.show();

                //Yes Button
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }

    Bitmap TextToImageEncode(String name) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    name,
                    BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }

}
