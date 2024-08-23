package smartdev.bzzhub.util;

import smartdev.bzzhub.BzzApp;
import smartdev.bzzhub.repository.model.CompanyDetailResponse;
import smartdev.bzzhub.repository.model.CompanyLoginResult;
import smartdev.bzzhub.repository.model.UserLoginResponse;
import smartdev.bzzhub.repository.model.UserProfileResponse;

import com.google.gson.Gson;

import static smartdev.bzzhub.repository.PreferenceKey.ARG_COMPANY_PROFILE;
import static smartdev.bzzhub.repository.PreferenceKey.ARG_USER_PROFILE;

public class ProfileUtils {

    public static void saveCompanyProfile(CompanyDetailResponse.CompanyDetail companyProfile){
        String json = new Gson().toJson(companyProfile);
        BzzApp.getPreferenceManager().put(ARG_COMPANY_PROFILE,json);
    }

    public static CompanyDetailResponse.CompanyDetail getCompanyProfile(){
        try {
            return new Gson().fromJson(BzzApp.getPreferenceManager()
                    .getString(ARG_COMPANY_PROFILE,""), CompanyDetailResponse.CompanyDetail.class);
        }catch (Exception e){
            return null;
        }
    }

    public static void saveUserProfile(UserProfileResponse.Result userProfile){
        String json = new Gson().toJson(userProfile);
        BzzApp.getPreferenceManager().put(ARG_USER_PROFILE,json);
    }

    public static UserProfileResponse.Result getUserProfile(){
        try {
            return new Gson().fromJson(BzzApp.getPreferenceManager()
                    .getString(ARG_USER_PROFILE,""), UserProfileResponse.Result.class);

        }catch (Exception e){
            return null;
        }
    }
}
