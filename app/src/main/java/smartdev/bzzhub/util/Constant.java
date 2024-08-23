package smartdev.bzzhub.util;

import android.content.Context;

import java.util.ArrayList;

import smartdev.bzzhub.repository.MyPreferenceManager;
import smartdev.bzzhub.repository.model.CityResponse;
import smartdev.bzzhub.repository.model.CommunityListResponse;
import smartdev.bzzhub.repository.model.CompanyDetailResponse;
import smartdev.bzzhub.repository.model.CompanyLoginResult;
import smartdev.bzzhub.repository.model.CountryResponse;
import smartdev.bzzhub.repository.model.SelectorResponse;
import smartdev.bzzhub.repository.model.UserLoginResponse;
import smartdev.bzzhub.repository.model.UserProfileResponse;

import static smartdev.bzzhub.repository.PreferenceKey.ARG_USER_LANGUAGE;

public class Constant {
    static Constant instance = null;
    private CompanyDetailResponse.CompanyDetail companyProfile = null;
    private UserProfileResponse.Result userProfile = null;
    private ArrayList<SelectorResponse.Result> selectorList =new ArrayList<>();
    private  ArrayList<CountryResponse.Result> countries =new ArrayList<>();
    private  ArrayList<CityResponse.Result> cities =new ArrayList<>();
    private ArrayList<CommunityListResponse.Result> communityList = new ArrayList<>();

    public Constant() {

    }

    public static Constant getInstance() {
        if (instance == null) {
            instance = new Constant();
        }
        return instance;
    }

    public static String getLanguage(Context mContext){
        if (MyPreferenceManager.getInstance(mContext).getInt(ARG_USER_LANGUAGE,2) == 2) {
            return "ar";
        }else {
            return "en";
        }
    }

    public CompanyDetailResponse.CompanyDetail getCompanyProfile() {
        if (ProfileUtils.getCompanyProfile() != null)
            return ProfileUtils.getCompanyProfile();
        else
            return companyProfile;
    }

    public UserProfileResponse.Result getUserProfile() {
        if (ProfileUtils.getUserProfile() != null)
            return ProfileUtils.getUserProfile();
        else
            return userProfile;
    }

    public void setCompanyProfile(CompanyDetailResponse.CompanyDetail companyProfile) {
        this.companyProfile = companyProfile;
    }

    public void setUserProfile(UserProfileResponse.Result userProfile) {
        this.userProfile = userProfile;
    }

    public Integer getLoginType(){
        if (getCompanyProfile() == null && getUserProfile() == null)
            return null;
        else if (getCompanyProfile() != null)
            return 1;
        else
            return 2;
    }

    public Integer getLoginID(){
        if (getLoginType() == null){
            return  0;
        }else {
            if (getLoginType() == 1){
                return getCompanyProfile().getCompanyId();
            }else {
                return getUserProfile().getUserId();
            }
        }
    }

    public String getLoginName(){
        if (getLoginType() == null){
            return  "";
        }else {
            if (getLoginType() == 1){
                return getCompanyProfile().getCompanyName();
            }else {
                return getUserProfile().getFullName();
            }
        }
    }


    public String getLoginImage(){
        if (getLoginType() == null){
            return  "";
        }else {
            if (getLoginType() == 1){
                return getCompanyProfile().getImage();
            }else {
                return getUserProfile().getImage();
            }
        }
    }

    public Integer getLoginFlag(){
        if (getLoginType() == null){
           return 3;
        } else if (getLoginType() == 1){
            return 1;
        }else {
            return 0;
        }
    }

    public ArrayList<SelectorResponse.Result> getSelectorList() {
        return selectorList;
    }

    public ArrayList<CountryResponse.Result> getCountries() {
        return countries;
    }

    public ArrayList<CityResponse.Result> getCities() {
        return cities;
    }

    public void setSelectorList(ArrayList<SelectorResponse.Result> selectorList) {
        this.selectorList = selectorList;
    }

    public void setCountries(ArrayList<CountryResponse.Result> countries) {
        this.countries = countries;
    }

    public void setCities(ArrayList<CityResponse.Result> cities) {
        this.cities = cities;
    }

    public ArrayList<CommunityListResponse.Result> getCommunityList() {
        return communityList;
    }

    public void setCommunityList(ArrayList<CommunityListResponse.Result> communityList) {
        this.communityList = communityList;
    }
}
