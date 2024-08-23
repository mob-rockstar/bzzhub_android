package smartdev.bzzhub.util;

import android.content.Context;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.R;

public class Validator {
    public static String validateLoginInfo(EditText editTextEmail,EditText editTextPassword){
        String strEmail =editTextEmail.getText().toString().trim();
        String strPassword = editTextPassword.getText().toString().trim();
        Context context = editTextEmail.getContext();
        if (strEmail.isEmpty())
            return context.getResources().getString(R.string.str_empty_email);
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())
            return context.getResources().getString(R.string.str_invalid_email);
        else if (strPassword.isEmpty())
            return context.getResources().getString(R.string.str_empty_password);
        else if (strPassword.length() < 6)
            return context.getResources().getString(R.string.str_invalid_password);
        else
            return "";
    }

    public static String validateUserRegisterInfo(EditText editTextName, EditText editTextEmail, EditText editTextMobile
    , EditText editTextPassword, EditText editTextConfirmPassword){
        String strName = editTextName.getText().toString().trim();
        String strEmail = editTextEmail.getText().toString().trim();
        String strMobile = editTextMobile.getText().toString().trim();
        String strPassword =  editTextPassword.getText().toString().trim();
        String strConfirmPassword = editTextConfirmPassword.getText().toString().trim();
        Context context = editTextEmail.getContext();
        if (strName.isEmpty()){
            return  context.getResources().getString(R.string.str_empty_name);
        }else if (strName.length() < 3){
            return  context.getResources().getString(R.string.str_invalid_name);
        }else if (strEmail.isEmpty())
            return context.getResources().getString(R.string.str_empty_email);
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())
            return context.getResources().getString(R.string.str_invalid_email);
        else if (strMobile.isEmpty())
            return context.getResources().getString(R.string.str_empty_mobile);
        else if (strMobile.length() < 8)
            return context.getResources().getString(R.string.str_invalid_mobile);
        else if (strPassword.isEmpty())
            return context.getResources().getString(R.string.str_empty_password);
        else if (strPassword.length() < 6)
            return context.getResources().getString(R.string.str_invalid_password);
        else if (!strPassword.equals(strConfirmPassword))
            return context.getResources().getString(R.string.str_not_match_password);
        else
            return "";
    }

    public static String validateCompanyRegisterInfo(EditText editTextName, EditText editTextEmail, EditText editTextMobile
            , EditText editTextPassword, EditText editTextConfirmPassword,Integer businessCategoryId){
        String strName = editTextName.getText().toString().trim();
        String strEmail = editTextEmail.getText().toString().trim();
        String strMobile = editTextMobile.getText().toString().trim();
        String strPassword =  editTextPassword.getText().toString().trim();
        String strConfirmPassword = editTextConfirmPassword.getText().toString().trim();
        Context context = editTextEmail.getContext();
        if (strName.isEmpty()){
            return  context.getResources().getString(R.string.str_empty_name);
        }else if (strName.length() < 3){
            return  context.getResources().getString(R.string.str_invalid_name);
        }else if (strEmail.isEmpty())
            return context.getResources().getString(R.string.str_empty_email);
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())
            return context.getResources().getString(R.string.str_invalid_email);
        else if (strMobile.isEmpty())
            return context.getResources().getString(R.string.str_empty_mobile);
        else if (strMobile.length() < 8)
            return context.getResources().getString(R.string.str_invalid_mobile);
        else if (strPassword.isEmpty())
            return context.getResources().getString(R.string.str_empty_password);
        else if (strPassword.length() < 6)
            return context.getResources().getString(R.string.str_invalid_password);
        else if (!strPassword.equals(strConfirmPassword))
            return context.getResources().getString(R.string.str_not_match_password);
        else if (businessCategoryId == 0){
            return context.getResources().getString(R.string.str_select_caetegory);
        }
        else
            return "";
    }

    public static String validateCompanyRFQ(String strName,String strMobile,String strKeyword,String strCategory,
                                            String strSubcategory,String strUnit,String strPiece){
        Context context = BzzApp.getCurrentActivity();
        if (strName.isEmpty()){
            return  context.getResources().getString(R.string.str_empty_name);
        }else if (strName.length() < 3){
            return  context.getResources().getString(R.string.str_invalid_name);
        } else if (strMobile.isEmpty())
            return context.getResources().getString(R.string.str_empty_mobile);
        else if (strMobile.length() < 8)
            return context.getResources().getString(R.string.str_invalid_mobile);
        else if (strKeyword.isEmpty())
                return "Please input keyword";
        else if (strCategory.isEmpty())
            return "Please select category";
        else if (strSubcategory.isEmpty())
            return "Please select subcategory";
        else if (strUnit.isEmpty())
            return "Please enter Unit";
        else if (strPiece.isEmpty())
            return "Please enter Piece";
        else return "";
    }

    public static String getYoutubePlaceholder (String youtubeURL){
        String pattern = "(?<=watch\\?v=|/videos/|youtu.be/|youtube.com/|embed\\/)[^#\\&\\?]*";


        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youtubeURL);

        if(matcher.find()){
            return  "https://img.youtube.com/vi/" + matcher.group();
        }else {
            return  "error";
        }
    }

    public static String getActualURL(String youtubeURL){
        String pattern = "(?<=watch\\?v=|/videos/|youtu.be/|youtube.com/|embed\\/)[^#\\&\\?]*";

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(youtubeURL);

        if(matcher.find()){
            return matcher.group();
        }else {
            return  "";
        }
    }

}
