package smartdev.bzzhub.repository.network;

import android.util.Log;

import com.makeramen.roundedimageview.RoundedImageView;

import smartdev.bzzhub.repository.MyPreferenceManager;
import smartdev.bzzhub.repository.model.AllCompanyResponse;
import smartdev.bzzhub.repository.model.ApplicantResponse;
import smartdev.bzzhub.repository.model.BookResponse;
import smartdev.bzzhub.repository.model.BusinessCategoryResponse;
import smartdev.bzzhub.repository.model.CityResponse;
import smartdev.bzzhub.repository.model.ColorResponse;
import smartdev.bzzhub.repository.model.CommunityDetailResponse;
import smartdev.bzzhub.repository.model.CommunityEventListResponse;
import smartdev.bzzhub.repository.model.CommunityEventResponse;
import smartdev.bzzhub.repository.model.CommunityFeedDetailResponse;
import smartdev.bzzhub.repository.model.CommunityFeedResponse;
import smartdev.bzzhub.repository.model.CommunityImageResponse;
import smartdev.bzzhub.repository.model.CommunityListResponse;
import smartdev.bzzhub.repository.model.CommunityMemberResponse;
import smartdev.bzzhub.repository.model.CompanyDetailResponse;
import smartdev.bzzhub.repository.model.CompanyJobResponse;
import smartdev.bzzhub.repository.model.CompanyLocationResponse;
import smartdev.bzzhub.repository.model.CompanyLoginResult;
import smartdev.bzzhub.repository.model.CompanyRegisterResponse;
import smartdev.bzzhub.repository.model.CompanyVerifyCodeResponse;
import smartdev.bzzhub.repository.model.ConnectionResponse;
import smartdev.bzzhub.repository.model.CountryResponse;
import smartdev.bzzhub.repository.model.DiscussionCommentResponse;
import smartdev.bzzhub.repository.model.DiscussionListResponse;
import smartdev.bzzhub.repository.model.FeedCommentResponse;
import smartdev.bzzhub.repository.model.InterestResponse;
import smartdev.bzzhub.repository.model.JobResponse;
import smartdev.bzzhub.repository.model.MainPageResponse;
import smartdev.bzzhub.repository.model.MediaSearchReponse;
import smartdev.bzzhub.repository.model.ProductDetail;
import smartdev.bzzhub.repository.model.ProfileMediaResponse;
import smartdev.bzzhub.repository.model.RFQDetailResponse;
import smartdev.bzzhub.repository.model.RFQResponse;
import smartdev.bzzhub.repository.model.RequestMsgResponse;
import smartdev.bzzhub.repository.model.SccidResponse;
import smartdev.bzzhub.repository.model.SelectorResponse;
import smartdev.bzzhub.repository.model.SimpleResponse;
import smartdev.bzzhub.repository.model.SizeResponse;
import smartdev.bzzhub.repository.model.SubCategoryResponse;
import smartdev.bzzhub.repository.model.UserJobResponse;
import smartdev.bzzhub.repository.model.UserLoginResponse;
import smartdev.bzzhub.repository.model.UserProfileFeedResponse;
import smartdev.bzzhub.repository.model.UserProfileResponse;
import smartdev.bzzhub.repository.model.UserProfileVisitorResponse;
import smartdev.bzzhub.repository.model.UserSignUpResponse;
import smartdev.bzzhub.repository.model.UserVerifyCodeResponse;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.networkutil.HttpUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static smartdev.bzzhub.repository.PreferenceKey.ARG_FIREBASE_TOKEN;
import static smartdev.bzzhub.util.RxUtils.applySchedulers;

public class BzHubRepository {

    private final BzHubAPI api;
    private final MyPreferenceManager pref;
    private final int device = 1;
    private final String X_API_KEY = "1b41924f23b544e8dc9dd2cbe2328ba0";

    public BzHubRepository(MyPreferenceManager preferences) {
        this.pref = preferences;
        api = HttpUtils.getRetrofit(BzHubURL.BASE_URL).create(BzHubAPI.class);
    }

    public BzHubAPI getApi() {
        return api;
    }

    public Observable<MainPageResponse> getCompanyList(int pageId, int pageLimit){
        return api.getCompanyList(X_API_KEY,pageId,pageLimit).compose(applySchedulers());
    }

    public Observable<CompanyDetailResponse> getCompanyDetail(int companyId){
        return api.getCompanyDetail(X_API_KEY,companyId).compose(applySchedulers());
    }

    public Observable<BookResponse> getBookList(int companyId){
        return api.getBookList(X_API_KEY,companyId).compose(applySchedulers());
    }

    public Observable<UserLoginResponse> loginUser(String email,String password,String language){
        return api.loginUser(X_API_KEY,email,password,getFirebaseToken(),language).compose(applySchedulers());
    }

    public Observable<UserLoginResponse> loginFacebookUser(String facebookId, String language){
        return api.loginFacebookUser(X_API_KEY,facebookId,getFirebaseToken(),language).compose(applySchedulers());
    }


    public Observable<CompanyLoginResult> loginCompany(String email, String password, String language){
        return api.loginCompany(X_API_KEY,email,password,getFirebaseToken(),language).compose(applySchedulers());
    }

    public Observable<CompanyLoginResult> loginFacebookCompany(String facebookId, String language){
        return api.loginFacebookCompany(X_API_KEY,facebookId,getFirebaseToken(),language).compose(applySchedulers());
    }


    public Observable<UserSignUpResponse> signUpWithUser(String name, String mobile, String email, String password,
                                                         double latitude, double longitude,String language,String facebookId){
        return api.signUpWithUser(X_API_KEY,name,email,mobile,password,getFirebaseToken(),device,latitude,longitude,language,facebookId).compose(applySchedulers());
    }

    public Observable<CompanyRegisterResponse> signUpWithCompany(String name, String mobile, String email, String password, int businessId,
                                                                 double latitude, double longitude, String language,String facebookId,
                                                                 int manufacturer, int trader, int serviceCompany){
        return api.signUpWithCompany(X_API_KEY,name,email,mobile,businessId,password,getFirebaseToken(),device,latitude,longitude, language, facebookId,
                manufacturer, trader, serviceCompany).compose(applySchedulers());
    }

    public Observable<BusinessCategoryResponse> getBusinessCategoryResponse(){
        return api.getBusinessCategories(X_API_KEY).compose(applySchedulers());
    }

    public Observable<SimpleResponse> forgotPasswordUser(String email){
        return api.forgetPasswordUser(X_API_KEY,email).compose(applySchedulers());
    }

    public Observable<SimpleResponse> forgotPasswordCompany(String email){
        return api.forgetPasswordCompany(X_API_KEY,email).compose(applySchedulers());
    }

    public Observable<UserVerifyCodeResponse> verifyCodeUser(String code){
        return api.verifyCodeUser(X_API_KEY,code).compose(applySchedulers());
    }

    public Observable<CompanyVerifyCodeResponse> verifyCodeCompany(String code){
        return api.verifyCodeCompany(X_API_KEY,code).compose(applySchedulers());
    }

    public Observable<SimpleResponse> resetPasswordUser(int companyId,String password){
        return api.resetPasswordUser(X_API_KEY,companyId,password).compose(applySchedulers());
    }

    public Observable<SimpleResponse> resetPasswordCompany(int companyId,String password){
        return api.resetPasswordCompany(X_API_KEY,companyId,password).compose(applySchedulers());
    }

    public Observable<ProfileMediaResponse> getCompanyMedias(int companyId){
        return api.getCompanyMediaList(X_API_KEY,companyId).compose(applySchedulers());
    }

    public Observable<CompanyLocationResponse> getCompanyLocation(int companyId){
        return api.getCompanyLocation(X_API_KEY,companyId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> createCompanyRFQ(Integer companyId, String name, String phone,
                                                       String email, String unit,
                                                       String qty, String description,String keyword,
                                                       File file,String sectorId,
                                                       int loginFlag, int loginId){
        RequestBody RcompanyId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(companyId));
        RequestBody Rname = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody Rphone = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody Remail = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody Runit = RequestBody.create(MediaType.parse("text/plain"), unit);
        RequestBody Rqty = RequestBody.create(MediaType.parse("text/plain"), qty);
        RequestBody RsectorId = RequestBody.create(MediaType.parse("text/plain"), sectorId);
        RequestBody Rdescription = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody RKeyword = RequestBody.create(MediaType.parse("text/plain"), keyword);
        RequestBody senderid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(loginId));
        RequestBody flag = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(loginFlag));
        MultipartBody.Part image = null;

        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"), file);
            image = MultipartBody.Part.createFormData("logo", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("logo", "", attachmentEmpty);
        }

        return api.submitCompanyRFQ(X_API_KEY,RcompanyId,Rname,Rphone,
                Remail,Runit,Rqty,RKeyword,Rdescription,image,RsectorId
        ,flag,senderid).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<SubCategoryResponse> getSubcategories(int categoryId){
        return api.getSubcategoriesList(X_API_KEY,categoryId).compose(applySchedulers());
    }

    public Observable<SelectorResponse> getSelectors(){
        return api.getSelectors(X_API_KEY).compose(applySchedulers());
    }

    public Observable<CountryResponse> getCountries(){
        return api.getCountries(X_API_KEY).compose(applySchedulers());
    }

    public Observable<CityResponse> getCities(int countryId){
        return api.getCity(X_API_KEY,countryId).compose(applySchedulers());
    }

    public Observable<MainPageResponse> searchCompany(int pageId, int pageLimit,String key,Integer selectorId,Integer cityId,
                                                      Integer countryId,Integer comapnyId ){
        return api.searchCompanyList(X_API_KEY,key,selectorId == null || selectorId == 0 ? "" : String.valueOf(selectorId),cityId == null || cityId == 0 ? "" : String.valueOf(cityId),
                countryId == null|| countryId == 0 ? "" : String.valueOf(countryId),pageId,pageLimit,comapnyId == null || comapnyId == 0? "" : String.valueOf(comapnyId)).compose(applySchedulers());
    }

    public Observable<SccidResponse> getSCCIDResponse(){
        return api.getSCCIDS(X_API_KEY).compose(applySchedulers());
    }

    public Observable<UserProfileResponse> getUserProfile(int userId){
        return api.getUserProfile(X_API_KEY,userId).compose(applySchedulers());
    }

    public Observable<UserProfileFeedResponse> getUserFeed(int id,int flag,int page,int perPage){
        return api.getUserFeed(X_API_KEY,id,flag,page).compose(applySchedulers());
    }

    public Observable<SimpleResponse> createUserFeed(String id,String description,int flag,File file){
        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody RDescription = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody RFlag = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(flag));

        MultipartBody.Part image = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"), file);
            image = MultipartBody.Part.createFormData("attach", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("attach", "", attachmentEmpty);
        }

        return api.addUserFeed(X_API_KEY,Rid,RFlag,RDescription,image).compose(applySchedulers());
    }



    public Observable<SimpleResponse> removeFeed(int id){
        return api.removeFeed(X_API_KEY,id).compose(applySchedulers());
    }

    public Observable<RequestMsgResponse> getFriends(int id){
        return api.getFriendsList(X_API_KEY,id).compose(applySchedulers());
    }

    public Observable<ProfileMediaResponse> getUserMediaList(int id){
        return api.getUserMedia(X_API_KEY,id).compose(applySchedulers());
    }

    public Observable<UserProfileVisitorResponse> getVisitors(int id){
        return api.getVisitors(X_API_KEY,id).compose(applySchedulers());
    }

    public Observable<SimpleResponse> uploadImage(String keyword,int flag,int isPrivate,File file){

        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(getUserId()));
        RequestBody RKeyword = RequestBody.create(MediaType.parse("text/plain"), keyword);
        RequestBody RFlag = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(flag));
        RequestBody RIsPrivate = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(isPrivate));

        MultipartBody.Part image = null;

        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"), file);
            image = MultipartBody.Part.createFormData("attach", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("attach", "", attachmentEmpty);
        }

        return api.uploadImage(X_API_KEY,Rid,RFlag,RKeyword,RIsPrivate,image).compose(applySchedulers());
    }

    public Observable<SimpleResponse> uploadCompanyImage(String companyId,String keyword,int flag,int isPrivate,File file){

        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), companyId);
        RequestBody RKeyword = RequestBody.create(MediaType.parse("text/plain"), keyword);
        RequestBody RFlag = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(flag));
        RequestBody RIsPrivate = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(isPrivate));

        MultipartBody.Part image = null;

        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"), file);
            image = MultipartBody.Part.createFormData("attach", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("attach", "", attachmentEmpty);
        }

        return api.uploadImage(X_API_KEY,Rid,RFlag,RKeyword,RIsPrivate,image).compose(applySchedulers());
    }

    public Observable<SimpleResponse> uploadVideo(String keyword,int flag,String URL,int isPrivate){
        return api.uploadVideo(X_API_KEY,(getUserId()),keyword,flag,URL,isPrivate).compose(applySchedulers());
    }

    public Observable<SimpleResponse> uploadCompanyVideo(String companyId,String keyword,int flag,String URL,int isPrivate){
        return api.uploadVideo(X_API_KEY,Integer.parseInt(companyId),keyword,flag,URL,isPrivate).compose(applySchedulers());
    }

    public Observable<SimpleResponse> deleteJob(int jobId){
        return api.deleteJob(X_API_KEY,jobId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> updateJob(int jobId,String jobTitle,int sectorId,String description,int cityId){
        return api.updateJob(X_API_KEY,jobId,jobTitle,sectorId,description,cityId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> createJob(String jobTitle,int sectorId,String description,int cityId){
        return api.createJob(X_API_KEY,(getUserId()),jobTitle,sectorId,description,cityId).compose(applySchedulers());
    }

    public Observable<UserJobResponse> getAllJobs(int userId){
        return api.getJobs(X_API_KEY,userId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> removeMedia(Integer mediaId){
        return api.removeMedia(X_API_KEY,mediaId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> acceptFriends(int contactId){
        return api.acceptRequest(X_API_KEY,getUserId(),contactId).compose(applySchedulers());
    }
    public Observable<SimpleResponse> sendRequests(Integer toUser){
        return api.sendRequest(X_API_KEY,getUserId(),toUser).compose(applySchedulers());
    }

    public Observable<InterestResponse> getInterestList(){
        return api.getAllInterest(X_API_KEY).compose(applySchedulers());
    }

    public Observable<SimpleResponse> updateCompanyInterests(Integer userId,String interests){
        return api.updateCompanyInterests(X_API_KEY,userId,interests).compose(applySchedulers());
    }

    public Observable<CompanyJobResponse> getCompanyJobs(Integer companyId){
        return api.getCompanyJobs(X_API_KEY,companyId).compose(applySchedulers());
    }

    public Observable<ConnectionResponse> getCompanyConnections(Integer companyId){
        return api.getCompanyConnections(X_API_KEY,companyId).compose(applySchedulers());
    }

    public Observable<RFQResponse> getCompanyRFQ(Integer companyId){
        return api.getCompanyRFQ(X_API_KEY,companyId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> addPDF(String companyId,String title,File file){
        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), companyId);
        RequestBody RDescription = RequestBody.create(MediaType.parse("text/plain"), title);

        MultipartBody.Part pdf = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"), file);
            pdf = MultipartBody.Part.createFormData("attach", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            pdf = MultipartBody.Part.createFormData("attach", "", attachmentEmpty);
        }

        return api.addPDF(X_API_KEY,Rid,RDescription,pdf).compose(applySchedulers());
    }


    public  Observable<SimpleResponse> removePDF(Integer tenderId){
        return api.deleteTender(X_API_KEY,tenderId).compose(applySchedulers());
    }

    public Observable<RFQDetailResponse> getRFQDetail(int rfqID){
        return api.getRFQDetail(X_API_KEY,(rfqID)).compose(applySchedulers());
    }

    public Observable<SimpleResponse> createCompanyJob(int companyId,int cityId,int sectorId,String jobtitle,
                                                       String salary,String strDescription,String strAge,String strVacancies,
                                                       String startDate,String strEndDate){
        return api.createJob(X_API_KEY,companyId,cityId,sectorId,jobtitle,salary,strDescription,strAge,strVacancies,startDate,strEndDate).compose(applySchedulers());
    }

    public Observable<SimpleResponse> updateCompanyJob(int jobId,int cityId,int sectorId,String jobtitle,
                                                       String salary,String strDescription,String strAge,String strVacancies,
                                                       String startDate,String strEndDate){
        return api.updateJob(X_API_KEY,jobId,cityId,sectorId,jobtitle,salary,strDescription,strAge,strVacancies,startDate,strEndDate).compose(applySchedulers());
    }

    public Observable<SimpleResponse> deleteCompanyJob(Integer jobId){
        return api.deleteCompanyJob(X_API_KEY,jobId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> uploadCompanyLogo(String companyId, File file,File bannerFile){

        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), companyId);
        MultipartBody.Part image = null;

        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"),  file);
            image = MultipartBody.Part.createFormData("logo", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("logo", "", attachmentEmpty);
        }
        MultipartBody.Part bannerImage = null;
        if (bannerFile != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"),  bannerFile);
            bannerImage = MultipartBody.Part.createFormData("banner", bannerFile.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            bannerImage = MultipartBody.Part.createFormData("banner", "", attachmentEmpty);
        }

        return api.uploadCompanyLogo(X_API_KEY,Rid,image,bannerImage).compose(applySchedulers());
    }

    public Observable<SimpleResponse> uploadUserLogo(String userId, File file,File bannerFile){

        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), userId);
        MultipartBody.Part image = null;

        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"), file);
            image = MultipartBody.Part.createFormData("logo", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("logo", "", attachmentEmpty);
        }

        MultipartBody.Part bannerImage = null;
        if (bannerFile != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"), bannerFile);
            bannerImage = MultipartBody.Part.createFormData("banner", bannerFile.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            bannerImage = MultipartBody.Part.createFormData("banner", "", attachmentEmpty);
        }

        return api.uploadUserLogo(X_API_KEY,Rid,image,bannerImage).compose(applySchedulers());
    }


    public Observable<SimpleResponse> removeInterest(Integer id){
        return api.deleteInterest(X_API_KEY,id).compose(applySchedulers());
    }

    public Observable<SimpleResponse> hideEmail(Integer id, int status){
            return api.hideEmail(X_API_KEY,id,status).compose(applySchedulers());
    }

    public Observable<SimpleResponse> hidePhone(Integer id, int status){
        return api.hidePhone(X_API_KEY,id,status).compose(applySchedulers());
    }

    public Observable<SimpleResponse> hideWork(Integer id, int status){
        return api.hideWork(X_API_KEY,id,status).compose(applySchedulers());
    }

    public Observable<SimpleResponse> hideSchool(Integer id, int status){
        return api.hideSchool(X_API_KEY,id,status).compose(applySchedulers());
    }

    public Observable<SimpleResponse> hideUniversity(Integer id, int status){
        return api.hideUniversity(X_API_KEY,id,status).compose(applySchedulers());
    }

    public Observable<SimpleResponse> removeCompanyInterest(Integer id){
        return api.deleteCompanyInterest(X_API_KEY,id).compose(applySchedulers());
    }

    public Observable<SimpleResponse> updateUserInterest(String interests){
        return api.updateUserInterest(X_API_KEY,Constant.getInstance().getUserProfile().getUserId(),interests).compose(applySchedulers());
    }

    public Observable<SimpleResponse> removeProduct(Integer productId){
        return api.deleteCompanyProduct(X_API_KEY,productId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> updateCompanyProfile(Integer companyId,Integer year,String website,String description,Integer sectorId,
                                                           Integer cityId,String postcode,String landline,int manufacturer, int trader, int serviceCompany){
        return api.updateCompanyProfile(X_API_KEY,companyId,year,website,description,sectorId,cityId,postcode,landline,manufacturer, trader, serviceCompany).compose(applySchedulers());
    }

    public Observable<ColorResponse> getColors(){
        return api.getColors(X_API_KEY).compose(applySchedulers());
    }

    public Observable<SimpleResponse> addProductImage(String companyId, String title, String price, String description
            , String colors, String size, List<String> pathList){

        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), companyId);
        RequestBody Rtitle = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody RPrice = RequestBody.create(MediaType.parse("text/plain"), price);
        RequestBody Rdescription = RequestBody.create(MediaType.parse("text/plain"), (description));
        RequestBody Rcolors = RequestBody.create(MediaType.parse("text/plain"), (colors));
        RequestBody Rsize = RequestBody.create(MediaType.parse("text/plain"), (size));


        MultipartBody.Part[] imageList = new MultipartBody.Part[pathList.size()];


        for (int i = 0; i < pathList.size(); i++){
            String path  = pathList.get(i);
            if (path != null && !path.isEmpty()) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"), new File(path));
                imageList[i] = MultipartBody.Part.createFormData("image[]", path, requestFile);
                Log.d("ImageURLPath", "url:" + path);
            } else {
                RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
                imageList[i] = MultipartBody.Part.createFormData("image[]", "", attachmentEmpty);
            }
        }

        return api.addProduct(X_API_KEY,Rid,Rtitle,RPrice,Rdescription,Rcolors,Rsize,imageList).compose(applySchedulers());
    }

    public  Observable<SizeResponse> getSizeList(){
        return api.getSize(X_API_KEY).compose(applySchedulers());
    }

    public Observable<SimpleResponse> updateProduct(Integer productId,String strTitle,String price,String description,String colors,String size){
        return api.updateProduct(X_API_KEY,productId,strTitle,price,description,colors,size).compose(applySchedulers());
    }

    public Observable<ProductDetail> getProductDetail(Integer productId){
        return api.getProductDetail(X_API_KEY,(productId)).compose(applySchedulers());
    }

    public Observable<SimpleResponse> addCompanyProductImage(String companyId, File file){

        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), companyId);
        MultipartBody.Part image = null;

        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"), file);
            image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("image", "", attachmentEmpty);
        }

        return api.addProductImage(X_API_KEY,Rid,image).compose(applySchedulers());
    }

    public Observable<SimpleResponse> editCompanyProductImage(String imageId, File file){

        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), imageId);
        MultipartBody.Part image = null;

        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"), file);
            image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("image", "", attachmentEmpty);
        }

        return api.updateProductImage(X_API_KEY,Rid,image).compose(applySchedulers());
    }

    public Observable<JobResponse> getAllJobList(int currentPage){
        return api.getAllJobs(X_API_KEY,currentPage,10).compose(applySchedulers());
    }

    public Observable<JobResponse> searchCompanyJobs(String searchKey,Integer sectorId,Integer cityId,Integer countryId,int currentPage){
        return api.searchCompanyJobs(X_API_KEY,searchKey,sectorId == null || sectorId == 0?"":String.valueOf(sectorId),
                cityId ==  null || cityId == 0 ?"":String.valueOf(cityId),countryId == null || countryId == 0?"":String.valueOf(countryId)
                ,currentPage,10).compose(applySchedulers());
    }

    public Observable<JobResponse> searchApplicantJobs(String searchKey,int sectorId,int cityId,int countryId,int currentPage){
        return api.searchApplicantJobs(X_API_KEY,searchKey,sectorId == 0?"":String.valueOf(sectorId),
                cityId == 0?"":String.valueOf(cityId),countryId == 0 ?"":String.valueOf(countryId),currentPage,10).compose(applySchedulers());
    }

    public Observable<SimpleResponse> updateUserProfile(Integer userId,String email,String phone,String languages,String school,String university,
                                                        String work,int countryId, int cityId){
        return api.updateUserProfile(X_API_KEY,(userId),email,phone,languages,school,university,work,String.valueOf(cityId)
                ,String.valueOf(countryId)).compose(applySchedulers());
    }

    public Observable<AllCompanyResponse> getAllCompanyList(){
        return api.getAllCompanies(X_API_KEY).compose(applySchedulers());
    }

    public Observable<ProfileMediaResponse> getAllMedias(){
        return api.getAllMedias(X_API_KEY).compose(applySchedulers());
    }

    public Observable<MediaSearchReponse> searchMedias(int imageOrVideo, int currentPage, int limit, String searchKey,
                                                       Integer sectorId, Integer interestID, Integer cityId, Integer countryID,
                                                       Integer companyID,int pageID){
        return api.searchMedias(X_API_KEY,imageOrVideo,currentPage,limit,searchKey,sectorId == null || sectorId == 0 ? "" : String.valueOf(sectorId),
                interestID == null || interestID == 0 ? "" : String.valueOf(interestID),cityId == null || cityId == 0 ? "" : String.valueOf(cityId)
                ,countryID == null || countryID ==  0 ? "" : String.valueOf(countryID),companyID == null || companyID == 0? "" : String.valueOf(companyID),pageID).compose(applySchedulers());
    }

    public Observable<ApplicantResponse> getApplicantDetail(String userId){
        return api.getApplicationDetail(X_API_KEY,userId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> connectToCompany(Integer companyId, Integer connectWith){
        return api.connectToCompany(X_API_KEY,companyId,connectWith).compose(applySchedulers());
    }

    public Observable<CommunityListResponse> getCommunities(int currentPage){
        return api.getCommunityList(X_API_KEY,currentPage).compose(applySchedulers());
    }

    public Observable<CommunityListResponse> searchCommunities(String searchKey, Integer sectorID){
        return api.searchCommunityList(X_API_KEY,searchKey,sectorID == 0 ? null : sectorID).compose(applySchedulers());
    }

    public Observable<CommunityDetailResponse> getCommunityDetail(int communityID){
        return api.getCommunityDetail(X_API_KEY,communityID,Constant.getInstance().getLoginID(),
                Constant.getInstance().getLoginFlag()).compose(applySchedulers());
    }

    public Observable<SimpleResponse> joinCommunity(int communityId, int memberId, int flag){
        return api.joinCommunity(X_API_KEY,communityId,memberId,flag).compose(applySchedulers());
    }

    public Observable<SimpleResponse> leaveCommunity(int communityId, int memberId, int flag){
        return api.leaveCommunity(X_API_KEY,communityId,memberId,flag).compose(applySchedulers());
    }

    public Observable<SimpleResponse> deleteCommunity(int communityId){
        return api.deleteCommunity(X_API_KEY,communityId).compose(applySchedulers());
    }

    public Observable<CommunityFeedResponse> getCommunityFeedList(int page){
        return api.getCommunityFeedList(X_API_KEY,page).compose(applySchedulers());
    }

    public Observable<SimpleResponse> writeFedComment(int feedId,int userOrCompany,String comment,int flag){
        return api.writeFeedComment(X_API_KEY,feedId,userOrCompany,comment,flag).compose(applySchedulers());
    }

    public Observable<DiscussionListResponse> getDiscussionList(int pageId){
        return api.getDiscussionList(X_API_KEY,pageId).compose(applySchedulers());
    }

    public Observable<CommunityEventListResponse> getEventLists(int pageId){
        return api.getEventList(X_API_KEY,pageId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> createCommunity(int sectorID,int userOrCompanyId,String title,String description,
                                                      int flag,int cityID,File file){
        RequestBody Rsector = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sectorID));
        RequestBody RTitle = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody Rdescription = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userOrCompanyId));
        RequestBody Rflag = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(flag));
        RequestBody Rcity = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cityID));

        MultipartBody.Part image = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("image", "", attachmentEmpty);
        }

        return api.createCommunity(X_API_KEY,Rid,Rsector,RTitle,Rdescription,Rflag,Rcity,image).compose(applySchedulers());
    }

    public Observable<SimpleResponse> editCommunity(int communityId,int sectorID,int userOrCompanyId,String title,String description,
                                                      int flag,int cityID,File file){
        RequestBody Rcommunity = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(communityId));
        RequestBody Rsector = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sectorID));
        RequestBody RTitle = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody Rdescription = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userOrCompanyId));
        RequestBody Rflag = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(flag));
        RequestBody Rcity = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cityID));

        MultipartBody.Part image = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("image", "", attachmentEmpty);
        }

        return api.editCommunity(X_API_KEY,Rcommunity,Rid,Rsector,RTitle,Rdescription,Rflag,Rcity,image).compose(applySchedulers());
    }

    public Observable<SimpleResponse> createNewsFeed(int communityId,int userOrCompanyId,String title,String description,
                                                      int flag,File file){
        RequestBody RcommunityId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(communityId));
        RequestBody RTitle = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody Rdescription = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userOrCompanyId));
        RequestBody Rflag = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(flag));

        MultipartBody.Part image = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("image", "", attachmentEmpty);
        }

        return api.createNewsFeed(X_API_KEY,Rid,RcommunityId,RTitle,Rdescription,Rflag,image).compose(applySchedulers());
    }

    public Observable<CommunityFeedDetailResponse> getFeedDetails(int feedId){
        return api.getFeedDetail(X_API_KEY,feedId).compose(applySchedulers());
    }

    public Observable<CommunityEventResponse> getCommunityEvents(int communityId, int pageId){
        return api.getCommunityEvents(X_API_KEY,communityId,pageId).compose(applySchedulers());
    }

    public Observable<FeedCommentResponse> getFeedComments(int feedId,int pageId){
        return api.getFeedComments(X_API_KEY,feedId,pageId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> createDiscussion(int communityId,String title,String description,int flag,int userOrCompany){
        return api.createDiscussion(X_API_KEY,communityId,title,description,flag,userOrCompany).compose(applySchedulers());
    }

    public Observable<DiscussionCommentResponse> getDiscussionReply(int page,int discussionId){
        return api.getDiscussionReplyList(X_API_KEY,page,discussionId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> createDiscussionReply(int discussinoId,String post,int userOrCommentId,int flag){
        return api.createDiscussionComment(X_API_KEY,discussinoId,post,userOrCommentId,flag).compose(applySchedulers());
    }

    public Observable<CommunityEventListResponse> getCommunityMainEvents(int page){
        return api.getCommunityMainEvents(X_API_KEY,page).compose(applySchedulers());
    }

    public Observable<CommunityImageResponse> getCommunityImages(int page){
        return api.getCommunityImageList(X_API_KEY,page).compose(applySchedulers());
    }

    public Observable<CommunityImageResponse> getCommunityVideos(int page){
        return api.getCommunityVideoList(X_API_KEY,page).compose(applySchedulers());
    }

    public Observable<SimpleResponse> uploadCommunityImage(String communityId,String userOrCompany,String keyword,int flag,File file){

        RequestBody RcommunityID = RequestBody.create(MediaType.parse("text/plain"), communityId);
        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), userOrCompany);
        RequestBody RKeyword = RequestBody.create(MediaType.parse("text/plain"), keyword);
        RequestBody RFlag = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(flag));

        MultipartBody.Part image = null;

        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("file/*"), file);
            image = MultipartBody.Part.createFormData("attach", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("attach", "", attachmentEmpty);
        }

        return api.uploadCommunityImage(X_API_KEY,Rid,RcommunityID,RFlag,RKeyword,image).compose(applySchedulers());
    }

    public Observable<SimpleResponse> uploadCommunityVideo(Integer userOrCompany,Integer communityId,String keyword,int flag,String URL,int isPrivate){
        return api.uploadCommunityVideo(X_API_KEY,(userOrCompany),communityId,keyword,flag,URL,isPrivate).compose(applySchedulers());
    }

    public Observable<CommunityMemberResponse> getCommunityMemberList(int communityId, int page){
        return api.getCommunityMemberLists(X_API_KEY,communityId,page).compose(applySchedulers());
    }

    public Observable<SimpleResponse> createEvent(int communityId,String eventDate,int userOrCompanyId,String title,String description,
                                                    int flag,int cityID,File file){
        RequestBody Rcommunity = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(communityId));
        RequestBody ReventDate = RequestBody.create(MediaType.parse("text/plain"), (eventDate));
        RequestBody RTitle = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody Rdescription = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody Rid = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(userOrCompanyId));
        RequestBody Rflag = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(flag));
        RequestBody Rcity = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cityID));

        MultipartBody.Part image = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("image", "", attachmentEmpty);
        }

       return api.createEvents(X_API_KEY,Rcommunity,RTitle,Rdescription,Rcity,ReventDate,Rflag,Rid, image).compose(applySchedulers());
    }

    public Observable<SimpleResponse> updateEvent(int eventId,String eventDate,String title,String description,int cityID,File file){
        RequestBody ReventId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(eventId));
        RequestBody ReventDate = RequestBody.create(MediaType.parse("text/plain"), (eventDate));
        RequestBody RTitle = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody Rdescription = RequestBody.create(MediaType.parse("text/plain"), description);

        RequestBody Rcity = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(cityID));

        MultipartBody.Part image = null;
        if (file != null) {
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            image = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("image/*"), "");
            image = MultipartBody.Part.createFormData("image", "", attachmentEmpty);
        }

        return api.editEvents(X_API_KEY,ReventId,RTitle,Rdescription,Rcity,ReventDate,image).compose(applySchedulers());
    }

    public Observable<SimpleResponse> deleteEvent(int eventId){
        return api.deleteEvent(X_API_KEY,eventId).compose(applySchedulers());
    }

    public Observable<SimpleResponse> changeLanguage(int id, int flag, String language){
        return api.updateLanguage(X_API_KEY,id,flag,language).compose(applySchedulers());
    }

    public Observable<SimpleResponse> chatInGroup(int communityId, int loginId, int flag, String message){
        return api.chatInGroup(X_API_KEY,communityId,loginId,flag, message).compose(applySchedulers());
    }

    private String getFirebaseToken(){
        return pref.getString(ARG_FIREBASE_TOKEN,"");
    }

    private Integer getUserId(){
        return Constant.getInstance().getUserProfile().getUserId();
    }
}
