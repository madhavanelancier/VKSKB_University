package com.vskub.certificate;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;

import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vskub.certificate.SpannableTextvw.Slice;
import com.vskub.certificate.SpannableTextvw.SpannableTextView;
import com.vskub.certificate.common.Appconstants;
import com.vskub.certificate.common.Connection;
import com.vskub.certificate.common.GifImageView;
import com.vskub.certificate.common.Utils;
import com.vskub.certificate.tabtargetview.TapTarget;
import com.vskub.certificate.tabtargetview.TapTargetSequence;
import com.vskub.certificate.tabtargetview.TapTargetView;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private final String LOGTAG = "QRCScanner-MainActivity";
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=0;
    TextView namekannada,nameenglish;
    HTextView textvw;
    String qrtype="";
    FrameLayout ugbacklay,cerbacklay;
    SpannableTextView textvw_ugcontent2,textvw_ugstudentdegree,textvw_ugstudentname,textvw_ugsturegno,textvw_ugstudentcollege,textvw_ugchallancename,
            textvw_ugcontent1,textvw_vicechallance;
    FrameLayout backlay,downloadlay;
    LinearLayout homelay,ugcertificatelay,certificat_lay,reglay,nmlay;
    Button button2;
    Animation animation1,animation2;
    TextView stu_regno,stu_name,stu_college,stu_year,stu_degree,textView;
    ImageView stu_image;
    Dialog progbar,success_dialog,done_dialog,error_dialog;
    GifImageView prog_gif;
    TextView progname;
    Utils utils;
    private String pdflink = "", downloadFileName = "";
    String certificatetype="",studentid="",qr_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        utils=new Utils(getApplicationContext());
        textvw=(HTextView)findViewById(R.id.button);
        backlay=(FrameLayout) findViewById(R.id.backlay);
        homelay=(LinearLayout) findViewById(R.id.homelay);
        ugcertificatelay=(LinearLayout) findViewById(R.id.ugcetificatelay);
        certificat_lay=(LinearLayout) findViewById(R.id.cetificatelay);
        reglay=(LinearLayout) findViewById(R.id.reglay);
        nmlay=(LinearLayout) findViewById(R.id.candlay);
        button2=(Button) findViewById(R.id.button2);
        textView=(TextView) findViewById(R.id.textView);
        textvw_ugcontent2=(SpannableTextView)findViewById(R.id.englishtext2);
        textvw_ugstudentdegree=(SpannableTextView)findViewById(R.id.degreetext);
        textvw_ugstudentname=(SpannableTextView)findViewById(R.id.nametext);
        textvw_ugsturegno=(SpannableTextView)findViewById(R.id.regnotext);
        textvw_ugstudentcollege=(SpannableTextView)findViewById(R.id.collegetext);
        textvw_ugchallancename=(SpannableTextView)findViewById(R.id.vicechallancetext);
        textvw_ugcontent1=(SpannableTextView)findViewById(R.id.englishtext1);
        textvw_vicechallance=(SpannableTextView)findViewById(R.id.vicechallance);
        downloadlay=(FrameLayout) findViewById(R.id.downloadlay);
        ugbacklay=(FrameLayout) findViewById(R.id.ugbacklay);
        cerbacklay=(FrameLayout) findViewById(R.id.cerbacklay);
        stu_college=(TextView)findViewById(R.id.scollege);
        stu_degree=(TextView)findViewById(R.id.sdegree);
        stu_name=(TextView)findViewById(R.id.sname);
        stu_regno=(TextView)findViewById(R.id.sregno);
        stu_year=(TextView)findViewById(R.id.sdate);
        stu_image=(ImageView) findViewById(R.id.studentimage);
        animation1=AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_come_up);
        animation2 =
                AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.slide_out_down);

        Handler animationCompleteCallBack = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Log.i("Log", "Animation Completed");
                return false;
            }
        });
        progbar = new Dialog(this);
        progbar.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progbar.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View popup = getLayoutInflater().inflate(R.layout.progress_layout, null);
        prog_gif=(GifImageView)popup.findViewById(R.id.GifImageView);
        progname=(TextView) popup.findViewById(R.id.progname);
        prog_gif.setGifImageResource(R.mipmap.progressbar);
        progbar.setContentView(popup);
        progbar.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        progbar.setCancelable(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textvw.setVisibility(View.VISIBLE);
                textvw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                textvw.reset("Welcome to u");
                textvw.setTextColor(Color.BLACK);
                textvw.setBackgroundColor(Color.TRANSPARENT);
                textvw.setTypeface(null);
                textvw.setAnimateType(HTextViewType.LINE);
                textvw.animateText("Click To Scan");
            }
        }, 700);

        backlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion >= Build.VERSION_CODES.M) {
            int hasLocationPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            int hasSMSPermission = checkSelfPermission(Manifest.permission.SEND_SMS);
            int hasSMSPermission1 = checkSelfPermission(Manifest.permission.READ_SMS);
            int hasSMSPermission2 = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            int hasAccessLocation = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            int hasGetAccounts = checkSelfPermission(Manifest.permission.GET_ACCOUNTS);
            int hasInternet = checkSelfPermission(Manifest.permission.INTERNET);
            int hasAccessNetwork = checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE);
            int hasAccounts = checkSelfPermission(Manifest.permission.ACCOUNT_MANAGER);
            int hasCamera = checkSelfPermission(Manifest.permission.CAMERA);
            int hasrecord = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            int hasReadContacts = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            int hasWriteContacts = checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
            int hasReadStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasWriteStorage = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int haswritecalendar = checkSelfPermission(Manifest.permission.WRITE_CALENDAR);
            int hasChangeNetwork = checkSelfPermission(Manifest.permission.CHANGE_NETWORK_STATE);
            int hasPhoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            int hasPhoneState1 = checkSelfPermission(Manifest.permission.MODIFY_PHONE_STATE);
            int hasvibrate = checkSelfPermission(Manifest.permission.VIBRATE);
            int haswake = checkSelfPermission(Manifest.permission.WAKE_LOCK);
            List<String> permissions = new ArrayList<String>();
            if (hasReadStorage != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.SEND_SMS);
            }
            if (hasSMSPermission1 != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_SMS);
            }
            if (hasSMSPermission2 != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECEIVE_SMS);
            }
            if (hasAccessLocation != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (hasGetAccounts != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.GET_ACCOUNTS);
            }
            if (hasInternet != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.INTERNET);
            }
            if (hasAccessNetwork != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
            }
            if (hasAccounts != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCOUNT_MANAGER);
            }
            if (hasChangeNetwork != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CHANGE_NETWORK_STATE);
            }
            if (hasWriteStorage != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (hasPhoneState != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (hasPhoneState1 != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.MODIFY_PHONE_STATE);
            }
            if (hasvibrate != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.VIBRATE);
            }
            if (hasCamera != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (hasrecord != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECORD_AUDIO);
            }
            if (haswake != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WAKE_LOCK);
            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 1);
            }
        }


        cerbacklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                certificat_lay.setVisibility(View.GONE);
                homelay.setVisibility(View.VISIBLE);
                //ugcertificatelay.setVisibility(View.GONE);
            }
        });


    }



    public boolean checkPermission2()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                    android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("To allow required permission");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                        }
                    });
                    android.app.AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void scanclick(View v){
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            certificatetype="";
            studentid="";
            qr_id="";
            stu_image.setVisibility(View.GONE);
            Intent i = new Intent(MainActivity.this, BarcodeScan.class);
            startActivityForResult(i, REQUEST_CODE_QR_SCAN);
            overridePendingTransition(R.anim.slide_come_right, R.anim.right_to_left);
        }
        else{
            Toast.makeText(this, "Unable to open scanner, Please allow the camera permission.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED){

            if(resultCode != Activity.RESULT_OK)
            {
                Log.d(LOGTAG,"COULD NOT GET A GOOD RESULT.");
                if(data==null)
                    return;
                //Getting the passed result
                String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
                Log.i("utilssresulttt",result+"        rrrrrr");
                if( result!=null)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Scan Error");
                    alertDialog.setMessage("QR Code could not be scanned");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                return;

            }
            if(requestCode == REQUEST_CODE_QR_SCAN) {
                if (data == null)
                    return;
                //Getting the passed result
                String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
                Log.i("utilsssresult", "Have scan result in your app activity : " + result);
           /* AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Scan result");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();*/
                stu_name.setText("");
                stu_year.setText("");
                stu_regno.setText("");
                stu_degree.setText("");
                stu_college.setText("");
                String scanvalue/*[]*/ = result/*.split("\n", 2)*/;
                if (!scanvalue.isEmpty()/*length>= 2*/ ) {
                    String scantype = ""/*scanvalue[1].replace("Type:", "")*/;
                    Log.i("utilllsssssvalue", scanvalue/*[0]*/.replace("QR No:", "") + "   " + /*scanvalue.length + "    " + */scantype);
                    homelay.setVisibility(View.GONE);
                    certificat_lay.setVisibility(View.VISIBLE);
                    //ugcertificatelay.setVisibility(View.VISIBLE);
                    progname.setText("Loading");
                    certificatetype = scantype;
                    studentid=scanvalue/*[0]*/.replace("QR No:", "").trim();
                    Spanned txt = Html.fromHtml("This is a genuine certificate issued by "+getString(R.string.english_title));
                    textView.setText(txt);

                    progbar.show();
                    CertificateTask task = new CertificateTask();
                    try {
                        task.execute(studentid, URLEncoder.encode(certificatetype, "UTF-8"));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        progbar.dismiss();
                        errorDialog();
                    }
                    //textView.setText("This is a genuine certificate and the certificate no is "+studentid+" is issue by "+getString(R.string.english_title)+".");
                    /*progbar.show();
                    CertificateTask task = new CertificateTask();
                    try {
                        task.execute(studentid, URLEncoder.encode(certificatetype, "UTF-8"));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        progbar.dismiss();
                        errorDialog();
                    }*/
                } else {
                    errorDialog();
                }
            }
            else{
                errorDialog();
            }
            // ugcertificate();

        }
    }
    public void ugcertificate(){


        ugcertificatelay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ugbacklay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        downloadlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(qrtype.equals("1")){
                    progname.setText("Downloading..");
                    progbar.show();
                /*GetPdfLinkTask task=new GetPdfLinkTask();
                task.execute();*/
                    Log.e("stuid",studentid);
                    pdflink = Appconstants.PDF_DOWNLOAD + "id=" + qr_id;

                /*if(qr_id.startsWith("P")){

                }
                else if(qr_id.startsWith("U")){
                    pdflink = Appconstants.PDF_DOWNLOAD2 + "id=" + studentid;

                }*/
                    downloadFileName = studentid+".pdf";
                    new DownloadingTask().execute();
                }
                else if(qrtype.equals("0")){
                    errorDialog_orange();
                }

                // downloadFileName = stu_regno.getText().toString().trim();

            }
        });

        textvw_ugstudentdegree.setText("BACHELOR OF SCIENCE IN NURSING (POST BASIC)");
        textvw_ugstudentname.setText("MANIKANDAN G");
        textvw_ugcontent2.addSlice(new Slice.Builder("in recognition of fulfillment of the requirements for the said Degree in the examination held during")
                .textColor(Color.parseColor("#666666"))
                .textSize(30)
                .build());
        textvw_ugcontent2.addSlice(new Slice.Builder(" May 2015")
                .textColor(Color.parseColor("#000000"))
                .style(Typeface.ITALIC)
                .textSize(30)
                .build());
        textvw_ugcontent2.addSlice(new Slice.Builder(" Given under the seal of the University, in the")
                .textColor(Color.parseColor("#666666"))
                .textSize(30)
                .build());
        textvw_ugcontent2.addSlice(new Slice.Builder(" 19")
                .textColor(Color.parseColor("#000000"))
                .textSize(30)
                .style(Typeface.ITALIC)
                .build());
        textvw_ugcontent2.addSlice(new Slice.Builder(" th")
                .textColor(Color.parseColor("#000000"))
                .textSize(22)
                .style(Typeface.ITALIC)
                .superscript()
                .build());
        textvw_ugcontent2.addSlice(new Slice.Builder(" Convocation held on")
                .textColor(Color.parseColor("#666666"))
                .textSize(30)
                .build());
        textvw_ugcontent2.addSlice(new Slice.Builder(" 19 April 2016")
                .textColor(Color.parseColor("#000000"))
                .textSize(30)
                .style(Typeface.ITALIC)
                .build());
        textvw_ugcontent2.display();
        textvw_ugsturegno.addSlice(new Slice.Builder("U12130")
                .textColor(getResources().getColor(R.color.green))
                .textSize(30)
                .build());
        textvw_ugsturegno.display();
        textvw_ugstudentcollege.addSlice(new Slice.Builder("AYYA NADAR JANAKI AMMAL COLLEGE,SIVAKASI")
                .textColor(Color.parseColor("#666666"))
                .textSize(30)
                .build());
        textvw_ugstudentcollege.display();
        textvw_ugchallancename.addSlice(new Slice.Builder("Dr.P.Ganesh")
                .textColor(getResources().getColor(R.color.colorred))
                .textSize(30)
                .style(Typeface.BOLD_ITALIC)
                .build());
        textvw_ugchallancename.display();
        textvw_ugcontent1.addSlice(new Slice.Builder("We, the Chancellor, the Pro-Chancellor. the Vice-Chancellor and the members of the Senate and the Syndicate confer")
                .textColor(Color.parseColor("#666666"))
                .textSize(30)
                .build());
        textvw_ugcontent1.display();
        textvw_vicechallance.addSlice(new Slice.Builder("Vice Chancellor")
                .textSize(32)
                .textColor(getResources().getColor(R.color.colorred))
                .build());
        textvw_vicechallance.display();


    }

    public void roundCircle(){
        // if(!utils.loadonetime().equalsIgnoreCase("1")) {
        utils.savePreferences("one","1");
        final TapTargetSequence sequence = new TapTargetSequence(this);
        final SpannableString spannedDesc = new SpannableString("If you want to download your certificate, click this button.");
        // spannedDesc.setSpan(new UnderlineSpan(), spannedDesc.length() - "TapTargetView".length(), spannedDesc.length(), 0);
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.downloadlay), "For Download", spannedDesc)
                .cancelable(true)
                .drawShadow(true)
                .titleTextDimen(R.dimen.title_text_size)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
                // .. which evidently starts the sequence we defined earlier
                sequence.start();

            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                //Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetViewSample", "You dismissed me :(");


            }

        });



        //}

    }

    public void success_errorDialog(final String msg){
        success_dialog = new Dialog(this);
        success_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        success_dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View popup = getLayoutInflater().inflate(R.layout.app_update_dialog, null);
        TextView title=(TextView) popup.findViewById(R.id.titletext);
        TextView content=(TextView) popup.findViewById(R.id.content);
        ImageView titleimg=(ImageView) popup.findViewById(R.id.titleimg);
        FloatingActionButton done=(FloatingActionButton)popup.findViewById(R.id.donebut);
        FloatingActionButton cancel=(FloatingActionButton)popup.findViewById(R.id.cancelbut);
        success_dialog.setContentView(popup);
        success_dialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        success_dialog.setCancelable(false);

        if(msg.equalsIgnoreCase("success")){
            title.setText("Success");
            titleimg.setImageResource(R.mipmap.success);
            content.setText("Certificate download successful. Do you want to view it");
        }
        else{
            title.setText("Failed");
            titleimg.setImageResource(R.mipmap.failure);
            content.setText("Downloading failed.Please try again.");
        }
        success_dialog.show();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(msg.equalsIgnoreCase("success")) {
                    success_dialog.dismiss();
                    String pathname=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath().replace("file://","");
                    File file = new File( pathname.replace("//","/")+"/"+getString(R.string.short_name)+"/"+downloadFileName);
                    Log.i("utilsssspathh",file.getAbsolutePath()+"         "+file.getPath());
                    TextView filepath=(TextView)findViewById(R.id.filepath);


                    Intent target = new Intent(Intent.ACTION_VIEW);
                    // target.setDataAndType(Uri.fromFile(file),"application/pdf");
                    //target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri apkURI = FileProvider.getUriForFile(
                            MainActivity.this,
                            getApplicationContext()
                                    .getPackageName() + ".provider", file);
                    target.setDataAndType(apkURI, "application/pdf");
                    target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    Intent intent = Intent.createChooser(target, "Open File");
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        // Instruct the user to install a PDF reader here, or something
                    }
                }
                else{
                    success_dialog.dismiss();
                    progname.setText("Downloading..");
                    progbar.show();
                    /*GetPdfLinkTask task=new GetPdfLinkTask();
                    task.execute();*/
                    pdflink = Appconstants.PDF_DOWNLOAD + "id=" + qr_id;

                    /*if(qr_id.startsWith("P")){

                    }
                    else if(qr_id.startsWith("U")){
                        pdflink = Appconstants.PDF_DOWNLOAD2 + "id=" + studentid;

                    }*/
                    downloadFileName = studentid+".pdf";
                    new DownloadingTask().execute();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                success_dialog.dismiss();
            }
        });
    }




    public void doneDialog(){
        done_dialog = new Dialog(this);
        done_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        done_dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View popup = getLayoutInflater().inflate(R.layout.done_dialog, null);
        GifImageView titleimg=(GifImageView) popup.findViewById(R.id.GifImageView);
        FloatingActionButton cancel=(FloatingActionButton)popup.findViewById(R.id.cancelbut);
        done_dialog.setContentView(popup);
        done_dialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;

        titleimg.setGifImageResource(R.mipmap.donegif);

        done_dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                done_dialog.dismiss();
            }
        });
    }

    public void errorDialog(){
        error_dialog = new Dialog(this);
        error_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        error_dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View popup = getLayoutInflater().inflate(R.layout.error_dialog, null);
        GifImageView titleimg=(GifImageView) popup.findViewById(R.id.GifImageView);
        FloatingActionButton cancel=(FloatingActionButton)popup.findViewById(R.id.donebut);
        error_dialog.setContentView(popup);
        error_dialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        error_dialog.setCancelable(false);
        titleimg.setGifImageResource(R.mipmap.closegif);

        error_dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                error_dialog.dismiss();
                errorMsg();
            }
        });
    }


    public void errorDialog_orange(){
        error_dialog = new Dialog(this);
        error_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        error_dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        View popup = getLayoutInflater().inflate(R.layout.second_dialog, null);
        GifImageView titleimg=(GifImageView) popup.findViewById(R.id.GifImageView);
        FloatingActionButton cancel=(FloatingActionButton)popup.findViewById(R.id.donebut);
        error_dialog.setContentView(popup);
        error_dialog.getWindow().getAttributes().windowAnimations = R.style.animationdialog;
        error_dialog.setCancelable(false);
        titleimg.setGifImageResource(R.mipmap.closegif);

        error_dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                error_dialog.dismiss();
                errorMsg();
            }
        });
    }

    private class CertificateTask extends AsyncTask<String, String ,String> {


        protected void onPreExecute() {

            Log.i("CertificateTask", "started");
        }

        @Override
        protected String doInBackground(final String... arg0) {
            String resp = null;

            try {
               /* JSONObject obj = new JSONObject();
                obj.put("id", arg0[0]);
                obj.put("type", arg0[1]);*/
                Connection con = new Connection();
                Log.i("utilssCertificate URL: ", Appconstants.SCAN_CERTIFICATE + "id="+arg0[0]/*+"&type="+arg0[1]*/);

                resp = con.connStringResponse(Appconstants.SCAN_CERTIFICATE + "id="+arg0[0]/*+"&type="+arg0[1]*/);

                return resp;
            } catch (Exception uee) {
                Log.i("utilssrespppppppppppppp", uee.getMessage() + "");
                // uee.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String resp) {
            Log.i("utilssspecial resp", resp + "");
            if (progbar != null && progbar.isShowing())
                progbar.dismiss();
            if (resp != null) {
                try {
                    JSONArray arr = new JSONArray(resp);
                    JSONObject json = arr.getJSONObject(0);
                    if (json.getString("Status").equals("Success")) {
                        JSONArray jarr = json.getJSONArray("Response");
                        certificat_lay.setVisibility(View.GONE);
                        ugcertificatelay.setVisibility(View.VISIBLE);
                        qrtype=json.getString("type");

                        if(qrtype.equals("0")){
                            nmlay.setVisibility(View.GONE);
                            reglay.setVisibility(View.GONE);
                        }
                        else{
                            nmlay.setVisibility(View.VISIBLE);
                            reglay.setVisibility(View.VISIBLE);
                        }

                        for (int i = 0; i < jarr.length(); i++) {
                            JSONObject object = jarr.getJSONObject(i);
                            ugcertificate();
                            stu_regno.setText(object.getString("reg_no"));
                            stu_name.setText(object.getString("name"));
                            qr_id=(object.getString("qr_id"));
                            textView.setVisibility(View.VISIBLE);
                            // stu_college.setText(object.getString("college_name"));
                            //stu_degree.setText(object.getString("degree"));
                            //stu_year.setText(object.getString("exam_date"));
                            if(!object.isNull("image")) {
                                stu_image.setVisibility(View.VISIBLE);
                                if (object.getString("image").trim().length() != 0) {
                                    Picasso.get().load(object.getString("image")).placeholder(R.mipmap.place_male).into(stu_image);
                                } else {
                                    Picasso.get().load(R.mipmap.place_male).into(stu_image);
                                }
                            }
                            else{
                                stu_image.setVisibility(View.GONE);
                            }
                            roundCircle();
                            doneDialog();

                        }

                    } else {
                        textView.setVisibility(View.INVISIBLE);

                        errorDialog();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("exce",e.toString());
                    Toast.makeText(MainActivity.this, "Data not linked, Please contact University", Toast.LENGTH_SHORT).show();
                    errorMsg();
                }

            } else {
                Toast.makeText(MainActivity.this, "Please check your network connection and try again", Toast.LENGTH_SHORT).show();
                errorMsg();
            }

        }
    }


    private class GetPdfLinkTask extends AsyncTask<String, String ,String> {


        protected void onPreExecute() {

            Log.i("GetPdfLinkTask", "started");
        }

        @Override
        protected String doInBackground(final String... arg0) {
            String resp = null;

            try {
                JSONObject obj = new JSONObject();
                obj.put("id", studentid);
                obj.put("type", certificatetype);
                Connection con = new Connection();
                if(studentid.startsWith("P")) {
                    Log.i("utilssCertificate URL: ", Appconstants.PDF_DOWNLOAD  + "id=" + studentid);
                    //resp = con.sendHttpPostjson2(Appconstants.PDF_DOWNLOAD,obj,"");
                    resp = con.connStringResponse(Appconstants.PDF_DOWNLOAD + "id=" + studentid /*+ "&type=" + certificatetype*/);
                }else{
                    Log.i("utilssCertificate URL: ", Appconstants.PDF_DOWNLOAD2  + "id=" + studentid);
                    //resp = con.sendHttpPostjson2(Appconstants.PDF_DOWNLOAD,obj,"");
                    resp = con.connStringResponse(Appconstants.PDF_DOWNLOAD2 + "id=" + studentid /*+ "&type=" + certificatetype*/);
                }

                return resp;
            } catch (Exception uee) {
                Log.i("utilssrespppppppppppppp", uee.getMessage() + "");
                // uee.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String resp) {
            Log.i("utilssspecial resp", resp + "");


            if (resp != null) {
                try {
                    JSONArray arr = new JSONArray(resp);
                    JSONObject json = arr.getJSONObject(0);
                    if (json.getString("Status").equals("Success")) {
                        JSONArray jarr = json.getJSONArray("Response");
                        JSONObject object = jarr.getJSONObject(0);
                        pdflink = object.getString("pdf");
                        downloadFileName = pdflink.substring(pdflink.lastIndexOf( '/' ),pdflink.length());//Create file name by picking download file name from URL

                        Log.e("utilssslink", downloadFileName);

                        //Start Downloading Task
                        new DownloadingTask().execute();


                    } else {
                        progbar.dismiss();
                        success_errorDialog("error");
                    }


                } catch (Exception e) {
                    progbar.dismiss();
                    e.printStackTrace();
                    success_errorDialog("error");
                }

            } else {
                progbar.dismiss();
                success_errorDialog("error");
            }

        }
    }

    private class DownloadingTask extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if (outputFile != null) {
                    DownloadCountTask task=new DownloadCountTask();
                    task.execute();

                } else {
                    progbar.dismiss();

                    pdflink = Appconstants.PDF_DOWNLOAD2 + "id=" + studentid;
                    downloadFileName = studentid+".pdf";
                    new DownloadingTask1().execute();

                }
            } catch (Exception e) {
                e.printStackTrace();
                progbar.dismiss();
                success_errorDialog("error");
            }



        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(pdflink);//Create Download URl
                Log.e("download1",pdflink);
                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("Message", "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                //Get File if SD card is present
                if (isSDCardPresent()) {

                    apkStorage = new File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    +"/"+getString(R.string.short_name));
                } else
                    Toast.makeText(MainActivity.this, "Oops!! There is no space.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e("File", "Directory Created.");
                }
                Log.i("apkkkkpathhhh",apkStorage+"    "+downloadFileName);

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e("File", "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e("file", "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }


    private class DownloadingTask1 extends AsyncTask<Void, Void, Void> {

        File apkStorage = null;
        File outputFile = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if (outputFile != null) {
                    DownloadCountTask task=new DownloadCountTask();
                    task.execute();

                } else {
                    progbar.dismiss();
                    success_errorDialog("error");

                }
            } catch (Exception e) {
                e.printStackTrace();
                progbar.dismiss();
                success_errorDialog("error");
            }



        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                URL url = new URL(pdflink);//Create Download URl
                Log.e("download2",pdflink);

                HttpURLConnection c = (HttpURLConnection) url.openConnection();//Open Url Connection
                c.setRequestMethod("GET");//Set Request Method to "GET" since we are grtting data
                c.connect();//connect the URL Connection

                //If Connection response is not OK then show Logs
                if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    Log.e("Message", "Server returned HTTP " + c.getResponseCode()
                            + " " + c.getResponseMessage());

                }


                //Get File if SD card is present
                if (isSDCardPresent()) {

                    apkStorage = new File(
                            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                                    +"/"+getString(R.string.short_name));
                } else
                    Toast.makeText(MainActivity.this, "Oops!! There is no space.", Toast.LENGTH_SHORT).show();

                //If File is not present create directory
                if (!apkStorage.exists()) {
                    apkStorage.mkdir();
                    Log.e("File", "Directory Created.");
                }
                Log.i("apkkkkpathhhh",apkStorage+"    "+downloadFileName);

                outputFile = new File(apkStorage, downloadFileName);//Create Output file in Main File

                //Create New File if not present
                if (!outputFile.exists()) {
                    outputFile.createNewFile();
                    Log.e("File", "File Created");
                }

                FileOutputStream fos = new FileOutputStream(outputFile);//Get OutputStream for NewFile Location

                InputStream is = c.getInputStream();//Get InputStream for connection

                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);//Write new file
                }

                //Close all connection after doing task
                fos.close();
                is.close();

            } catch (Exception e) {

                //Read exception if something went wrong
                e.printStackTrace();
                outputFile = null;
                Log.e("file", "Download Error Exception " + e.getMessage());
            }

            return null;
        }
    }


    private class DownloadCountTask extends AsyncTask<String, String ,String> {


        protected void onPreExecute() {

            Log.i("DownloadCountTask", "started");
        }

        @Override
        protected String doInBackground(final String... arg0) {
            String resp = null;

            try {
                JSONObject obj = new JSONObject();
                obj.put("type", certificatetype);
                Connection con = new Connection();
                Log.i("utilssCertificate URL: ", Appconstants.DOWNLOAD_COUNT + "   " + obj.toString());

                resp = con.sendHttpPostjson2(Appconstants.DOWNLOAD_COUNT, obj, "");

                return resp;
            } catch (Exception uee) {
                Log.i("utilssrespppppppppppppp", uee.getMessage() + "");
                // uee.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String resp) {
            Log.i("utilssspecial resp", resp + "");
            if (progbar != null && progbar.isShowing())
                progbar.dismiss();
            success_errorDialog("success");

        }
    }
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
    public void errorMsg(){
        certificat_lay.setVisibility(View.GONE);
        ugcertificatelay.setVisibility(View.GONE);
        homelay.setVisibility(View.VISIBLE);
        stu_image.setVisibility(View.GONE);
        certificatetype="";
        studentid="";
        Intent i = new Intent(MainActivity.this, BarcodeScan.class);
        startActivityForResult(i, REQUEST_CODE_QR_SCAN);
        overridePendingTransition(R.anim.slide_come_right, R.anim.right_to_left);
    }

    @Override
    public void onBackPressed() {
        if(ugcertificatelay.getVisibility()==View.VISIBLE){
            ugcertificatelay.startAnimation(animation2);
            ugcertificatelay.setVisibility(View.GONE);
            homelay.setVisibility(View.VISIBLE);
            homelay.startAnimation(animation1);
            certificatetype="";
            studentid="";
        }
        else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_come_up, R.anim.slide_out_down);
        }
    }
}
