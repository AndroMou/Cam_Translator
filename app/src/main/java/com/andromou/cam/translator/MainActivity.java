package com.andromou.cam.translator;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentifier;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizerOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    EditText editTextView, editTextSaveName;
    Dialog dialogMore, dialogChoose, dialogSpeak, dialogWait, dialogSave;
    AdManager adManager;
    AdView mAdView;
    public static String txt;
    String text;
    EditText et;
    TextView tv;
    public static boolean bboolEdit = false;
    TextView tvLang;
    private static String languageCode, langCode;
    private static int interstitialAd = 0;
    float speed, pitch;
    SeekBar sbpitch, sbspeed;
    private static boolean boolplay = false;
    private static Locale intLocal;
    MyClipboardManager myClipboardManager;
    private TextToSpeech textToSpeech;
   // WebView textView;
   Toast toastActivate;
   TextView description, tvtextView;
   View payout, chayout;
   Uri image_uri;
   static String currentPhotoPath;
   public static String mText = "";
   Dialog dialogLangChooser;
   private final int REQUEST_CODE_STOREGE_PERMESSION = 1;
   private final int REQUEST_CODE_CAPTURE_PERMESSION = 6;
   private final int REQUEST_CODE_WRITE_EXTERNAL_PERMESSION = 1200;
   private final int REQUEST_CODE_SELECT_IMAGE = 2;
   private final int REQUEST_CODE_CAPTURE_IMAGE = 3;
    AnimationDrawable animationDrawable, animationDrawable1;
    public static int anInt = 0;
    private InterstitialAd mInterstitialAd;

    String interstitiel_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_main);
  //      compart();
        myClipboardManager = new MyClipboardManager();
        imageView = findViewById(R.id.imageView);
        tvtextView = findViewById(R.id.tvwebView);
        editTextView = findViewById(R.id.editwebView);
        sbpitch = findViewById(R.id.seekbar_pitch);
        sbspeed = findViewById(R.id.seekbar_speed);
  /*      mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();

        interstitiel_id = getResources().getString(R.string.id_admob_inter);
        InterstitialAd.load(this,interstitiel_id, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        // Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        // Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(adRequest);



        */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                //grant the permission
                requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAPTURE_PERMESSION);
            }
        }


    }


    public void onPickImage(View view) {
        showDialogs(view);
        interstitialShow();
        interstitialAd++;

    }

    @Override
    protected void onResume() {
        super.onResume();
        animationDrawable = (AnimationDrawable) getResources().getDrawable(R.drawable.killerflying);
        ((ImageView) findViewById(R.id.imageView)).setImageDrawable(animationDrawable);
        animationDrawable.start();
        animationDrawable1 = (AnimationDrawable) getResources().getDrawable(R.drawable.bg_textview_utile);
        ((ImageView) findViewById(R.id.imageView1)).setImageDrawable(animationDrawable1);
        animationDrawable1.start();

    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_STOREGE_PERMESSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_CAPTURE_PERMESSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                createImageFile();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_PERMESSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                makeFileTxt(mText);
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

     @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            CropImage.activity(data.getData())
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }
        else if (requestCode == REQUEST_CODE_CAPTURE_IMAGE && resultCode == RESULT_OK){
            CropImage.activity(image_uri)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

        // get crop image
         if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
             CropImage.ActivityResult result = CropImage.getActivityResult(data);
             if (resultCode == RESULT_OK){
                 Uri uri = result.getUri();
                 imageView.setImageURI(uri);
                 try {
                     BitmapDrawable bd = (BitmapDrawable)imageView.getDrawable();
                     Bitmap b = bd.getBitmap();
                     ml(b);
                   //  extraire(b);
                 }catch (Exception e){
                    e.printStackTrace();
                 }

             }
         }

    }



    public void ml(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
        imageView.setImageBitmap(bitmap);
        com.google.mlkit.vision.text.TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        Task<com.google.mlkit.vision.text.Text> result =
                recognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<com.google.mlkit.vision.text.Text>() {
                            @Override
                            public void onSuccess(Text visionText) {
                                // Task completed successfully
                                // ...
                                txt = visionText.getText();
                                tvtextView.setVisibility(View.VISIBLE);
                                editTextView.setVisibility(View.INVISIBLE);
                                tvtextView.setText(txt);
                                editTextView.setText(txt);
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        // Task failed with an exception
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                });

        /*
        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVision firebaseVision = FirebaseVision.getInstance();
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = firebaseVision.getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> task = firebaseVisionTextRecognizer.processImage(firebaseVisionImage);
        task.addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                txt = firebaseVisionText.getText();
                tvtextView.setVisibility(View.VISIBLE);
                editTextView.setVisibility(View.INVISIBLE);
                tvtextView.setText(txt);
                editTextView.setText(txt);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

         */
    }



    public void btnMore(View view) {
        showDialogs(view);
        interstitialShow();
        interstitialAd++;
    }

    public void onClickShare(View view) {
        interstitialShow();
        interstitialAd++;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Image to text - AI app on Google Play \n\n" + "https://play.google.com/store/apps/developer?id=5354191558196159558";
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public void onClickRate(View view) {
        interstitialShow();
        interstitialAd++;
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }
    // share text
    public void onClickShareText(View view) {
        interstitialShow();
        interstitialAd++;
        String text = txt;
        if (bboolEdit){
            text = editTextView.getText().toString();
            bboolEdit = false;
        }
        Intent i = new Intent();
        i.setAction(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_TEXT, text);
        i.setType("text/plain");
        i = Intent.createChooser(i, "Share By");
        startActivity(i);
    }

    public void onClickCopy(View view) {
        interstitialShow();
        interstitialAd++;
        String text = txt;
        if (bboolEdit){
            text = editTextView.getText().toString();
            bboolEdit = false;
        }
        myClipboardManager.copyToClipboard(MainActivity.this, text);
        // Toast.makeText(MainActivity.this, R.string.copy_text_to_clipboard, Toast.LENGTH_SHORT).show();
        afficher(R.layout.custom_toast_activated, R.id.toast_layout_root, R.id.description,
                        R.string.c_txt_clioard);
    }

    public void onClickGallory(View view) {
        interstitialShow();
        interstitialAd++;
        dialogChoose.dismiss();
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_STOREGE_PERMESSION);
        }else{
            selectImage();
        }
    }

    public void onClickCamera(View view) {
        dialogChoose.dismiss();
        try {
            createImageFile();
        } catch (ActivityNotFoundException e) {
            // display error state to the user

        }
    }

    public void  afficher(int ly, int ly_root, int desc, int stDEsc){
        LayoutInflater inflater = getLayoutInflater();
        View layoutD = inflater.inflate(ly, (ViewGroup) findViewById(ly_root));
        description = layoutD.findViewById(desc);
        description.setText(stDEsc);
        // Create and show the Toast object
        toastActivate = new Toast(getApplicationContext());
        toastActivate.setGravity(Gravity.CENTER, 0, 0);
        toastActivate.setDuration(Toast.LENGTH_LONG);
        toastActivate.setView(layoutD);
        toastActivate.show();
    }

    public void onClickTextEdit(View view) {
        interstitialShow();
        interstitialAd++;
        bboolEdit = true;
        tvtextView.setVisibility(View.INVISIBLE);
        editTextView.setVisibility(View.VISIBLE);
        editTextView.setText(txt);
    }

    private void showDialogs(View v){
        switch (v.getId()){
            case R.id.btnMore:
                dialogMore = new Dialog(this);
                dialogMore.setContentView(R.layout.custom_dialog_more);
                dialogMore.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogMore.show();
                break;

            case R.id.imageView:
                dialogChoose = new Dialog(this);
                dialogChoose.setContentView(R.layout.custom_dialog_choose);
                dialogChoose.show();
                break;

            case R.id.btnTranslate:
                identifyLanguage();
                dialogLangChooser = new Dialog(this);
                dialogLangChooser.setContentView(R.layout.dialog_language_chooser);
                dialogLangChooser.show();
                break;
        }

    }

    @Override
    protected void onDestroy() {
         if (textToSpeech != null){
             textToSpeech.stop();
             textToSpeech.shutdown();
         }
        super.onDestroy();
    }

    public void onClickPlay(View view) {
        interstitialShow();
        interstitialAd++;
        dialogLangChooser = new Dialog(this);
        dialogLangChooser.setContentView(R.layout.dialog_language_chooser);
        tvLang = dialogLangChooser.findViewById(R.id.txtlang);
        identifyLanguage();
        dialogLangChooser.show();
        identifyLanguage();

    }

    public void onClickDismiss(View view) {
        if (textToSpeech != null){
            boolplay = false;
            textToSpeech.stop();
            textToSpeech.shutdown();
            dialogSpeak.dismiss();
        }else
            dialogSpeak.dismiss();
    }



    ////translate
    private void identifyLanguage() {
        LanguageIdentifier identifier = LanguageIdentification.getClient();
        if (txt == null) txt = "Try again.!!";
        identifier.identifyLanguage(txt).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s.equals("und")){
                   // afficher(R.layout.custom_toast_langage, R.id.toast_layout_language, R.id.descriptionLanguage, R.string.noidenlang);
                   // Toast.makeText(getApplicationContext(),"Language Not Identified",Toast.LENGTH_SHORT).show();
                }else {
                    getLanguageCode(s);
                }
            }
        });
    }

    private void getLanguageCode(String language) {
        switch (language){
            case "de":
                langCode = TranslateLanguage.GERMAN;
                languageCode = getString(R.string.delang);
                break;
            case "en":
                langCode = TranslateLanguage.ENGLISH;
                languageCode = getString(R.string.englang);
                break;
            case "fr":
                langCode = TranslateLanguage.FRENCH;
                languageCode = getString(R.string.frlang);
                break;
            case "it":
                langCode = TranslateLanguage.ITALIAN;
                languageCode = getString(R.string.itlang);
                break;
            case "es":
                langCode = TranslateLanguage.SPANISH;
                languageCode = getString(R.string.eslang);
                break;
            case "pt":
                langCode = TranslateLanguage.PORTUGUESE;
                languageCode = getString(R.string.ptlang);
                break;
            default:
                languageCode = getString(R.string.noidenlang);
                langCode = "Unknown";
        }
       tvLang.setText(languageCode);
       // translateText(langCode);
    }

    private void translateText(String langCode, String targetText) {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(langCode)
                .setTargetLanguage(targetText)
                .build();
        final Translator translator = Translation.getClient(options);
        dialogWait = new Dialog(this);
        dialogWait.setCancelable(false);
        dialogWait.setContentView(R.layout.dialog_download_model);
        dialogWait.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogWait.show();
        DownloadConditions conditions = new DownloadConditions.Builder().requireWifi().build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dialogWait.dismiss();
                translator.translate(txt).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        txt = s;
                        tvtextView.setText(s);
                        editTextView.setText(s);
                        tvtextView.setVisibility(View.VISIBLE);
                        editTextView.setVisibility(View.INVISIBLE);
                    }
                });
            }
        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Model couldn’t be downloaded or other internal error.
                                dialogWait.dismiss();
                                Toast.makeText(getApplicationContext(), R.string.neee_dow_Library, Toast.LENGTH_LONG).show();
                            }
                        });
    }



    public void onTranslate(View view) {
        switch (view.getId()){
            case R.id.btnLangAr:
                translateText(langCode, TranslateLanguage.ARABIC);
                dialogLangChooser.dismiss();
                break;
            case R.id.btnLangEn:
                translateText(langCode, TranslateLanguage.ENGLISH);
                dialogLangChooser.dismiss();
                break;
            case R.id.btnLangFr:
                translateText(langCode, TranslateLanguage.FRENCH);
                dialogLangChooser.dismiss();
                break;
            case R.id.btnLangIt:
                translateText(langCode, TranslateLanguage.ITALIAN);
                dialogLangChooser.dismiss();
                break;
            case R.id.btnLangDe:
                translateText(langCode, TranslateLanguage.GERMAN);
                dialogLangChooser.dismiss();
                break;
            case R.id.btnLangEs:
                translateText(langCode, TranslateLanguage.SPANISH);
                dialogLangChooser.dismiss();
                break;
            case R.id.btnLangPt:
                translateText(langCode, TranslateLanguage.PORTUGUESE);
                dialogLangChooser.dismiss();
                break;
        }
    }

    public void onClickDismissDialog(View view) {
        dialogWait.dismiss();
    }

    private void createImageFile() {
        String imageFileName = "photo";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = null;
        try {
            image = File.createTempFile( imageFileName,".jpg", storageDir);
            currentPhotoPath = image.getAbsolutePath();
            image_uri = FileProvider.getUriForFile(MainActivity.this,
                    "com.andromou.cam.translator.fileprovider", image);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
            startActivityForResult(cameraIntent, REQUEST_CODE_CAPTURE_IMAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Save a file: path for use with ACTION_VIEW intents
    }

    private void extraire(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (textRecognizer.isOperational()){
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < items.size(); i++){
                TextBlock item = items.valueAt(i);
                stringBuilder.append(item.getValue());
                stringBuilder.append("\n");
            }
            tvtextView.setVisibility(View.VISIBLE);
            editTextView.setVisibility(View.INVISIBLE);
            tvtextView.setText(stringBuilder.toString());
            editTextView.setText(stringBuilder.toString());
        }
    }

    public void makeFileTxt(String text){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_mmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        String fileName = editTextSaveName.getText().toString().trim();
        if (!fileName.isEmpty()) {
            fileName = fileName + ".txt";
        }else fileName = "MyFile_" + timeStamp + ".txt";


        try {
            String directory = "/ScannerApp ";
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + directory;

            File dir = new File(path);
           // File path = Environment.getStorageDirectory();
           // File dir = new File(path + "/My Files/");
            if(!dir.exists())           //check if not created then create the firectory
                dir.mkdirs();
           // String fileName = "MyFile_" + timeStamp + ".txt";
            File file = new File(dir, fileName);
            FileWriter fileWriter = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(text);
            bufferedWriter.close();
            TextView tvdir = findViewById(R.id.tvdir);
          //  tvdir.setVisibility(View.VISIBLE);
            tvdir.setText( " تم حفظ الملف  " +"\n"+ fileName +"\n" +dir);
            Toast.makeText(this, fileName + " is saved to\n" +dir, Toast.LENGTH_LONG).show();

        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

        }
    }

    public void onSaveText(View view) {
        interstitialShow();
        interstitialAd++;

        mText = editTextView.getText().toString().trim();
        if (mText.isEmpty()){
            Toast.makeText(this, "Please write some thing....", Toast.LENGTH_LONG).show();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    //grant the permission
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_PERMESSION);
                } else {
                    makeFileTxt(mText);
                }
            } else {
                makeFileTxt(mText);
            }
        }
        dialogSave.dismiss();
    }

    public void btnclickDir(View view) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/ScannerApp/";
        String lien = "Android/storage/emulated/0/ScannerApp/";
        Uri uri = Uri.parse(lien);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
       // intent.setDataAndType(uri, "resource/folder");
        intent.setDataAndType(uri, "*/*");
        startActivity(intent);
    }

    //onSaveText
    public void onSaveFile(View view){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_mmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        String fileName = "My File_" + timeStamp;
        dialogSave = new Dialog(this);
        dialogSave.setContentView(R.layout.custom_dialog_save);
        editTextSaveName = dialogSave.findViewById(R.id.editSaveName);
        editTextSaveName.setText(fileName);
        dialogSave.show();

    }

    public void onClickSpeak(View view) {
        pitch = (float)sbpitch.getProgress() /50;
        if (pitch < 0.1) pitch = 0.1f;
        speed = (float)sbspeed.getProgress() /50;
        if (speed < 0.1) speed = 0.1f;
        textToSpeech.setPitch(pitch);
        textToSpeech.setSpeechRate(speed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(txt, TextToSpeech.QUEUE_FLUSH,null, null);
        }
    }

    public void interstitialShow(){
        if (interstitialAd % 10 == 0){
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MainActivity.this);
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }
        }
    }

}