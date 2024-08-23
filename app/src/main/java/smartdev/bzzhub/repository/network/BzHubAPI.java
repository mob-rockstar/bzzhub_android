package smartdev.bzzhub.repository.network;

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
import smartdev.bzzhub.repository.model.CompanyMediaResponse;
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

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import smartdev.bzzhub.repository.model.UserVerifyCodeResponse;

public interface BzHubAPI {

    @FormUrlEncoded
    @POST("main/all")
    Observable<MainPageResponse> getCompanyList(@Header("X-API-KEY") String ApiKey, @Field("currentpage") int pageId, @Field("limit") int pageLimit);

    @GET("company/home/{id}")
    Observable<CompanyDetailResponse> getCompanyDetail(@Header("X-API-KEY") String ApiKey, @Path("id") int id);

    @GET("company/tender/{id}")
    Observable<BookResponse> getBookList(@Header("X-API-KEY") String ApiKey, @Path("id") int id);

    @FormUrlEncoded
    @POST("user/login")
    Observable<UserLoginResponse> loginUser(@Header("X-API-KEY") String ApiKey,
                                            @Field("email") String email, @Field("password") String password,
                                            @Field("token") String token,@Field("language") String language);

    @FormUrlEncoded
    @POST("user/facebook/login")
    Observable<UserLoginResponse> loginFacebookUser(@Header("X-API-KEY") String ApiKey,
                                            @Field("facebook_id") String facebookId,
                                            @Field("token") String token,@Field("language") String language);

    @FormUrlEncoded
    @POST("company/login")
    Observable<CompanyLoginResult> loginCompany(@Header("X-API-KEY") String ApiKey, @Field("email") String email, @Field("password") String password,@Field("token") String token,@Field("language") String language);

    @FormUrlEncoded
    @POST("company/facebook/login")
    Observable<CompanyLoginResult> loginFacebookCompany(@Header("X-API-KEY") String ApiKey,  @Field("facebook_id") String facebookId,@Field("token") String token,@Field("language") String language);


    @FormUrlEncoded
    @POST("user/register")
    Observable<UserSignUpResponse> signUpWithUser(@Header("X-API-KEY") String ApiKey, @Field("fullname") String fullname,
                                                  @Field("email") String email, @Field("mobile") String mobile,
                                                  @Field("password") String password, @Field("token") String token,
                                                  @Field("device_type") int device, @Field("latitude") double latitude,
                                                  @Field("longitude") double longitude,@Field("language") String language,
                                                  @Field("facebook_id") String facebookId);

    @FormUrlEncoded
    @POST("company/register")
    Observable<CompanyRegisterResponse> signUpWithCompany(@Header("X-API-KEY") String ApiKey, @Field("company_name") String companyName, @Field("email") String email
            , @Field("mobile") String mobile, @Field("business_id") int businessId, @Field("password") String password, @Field("token") String token,
                                                          @Field("device_type") int device, @Field("latitude") double latitude,
                                                          @Field("longitude") double longitude,@Field("language") String language,
                                                          @Field("facebook_id") String facebookId,
                                                          @Field("manufacturer") int manufacturer,
                                                          @Field("trader") int trader,
                                                          @Field("serviceCompany") int serviceCompany);

    @GET("categories/get")
    Observable<BusinessCategoryResponse> getBusinessCategories(@Header("X-API-KEY") String ApiKey);

    @FormUrlEncoded
    @POST("user/forgetpassword")
    Observable<SimpleResponse> forgetPasswordUser(@Header("X-API-KEY") String ApiKey, @Field("email") String email);

    @FormUrlEncoded
    @POST("company/forgetpassword")
    Observable<SimpleResponse> forgetPasswordCompany(@Header("X-API-KEY") String ApiKey, @Field("email") String email);

    @FormUrlEncoded
    @POST("user/verify")
    Observable<UserVerifyCodeResponse> verifyCodeUser(@Header("X-API-KEY") String ApiKey, @Field("code") String code);

    @FormUrlEncoded
    @POST("company/verify")
    Observable<CompanyVerifyCodeResponse> verifyCodeCompany(@Header("X-API-KEY") String ApiKey, @Field("code") String code);

    @FormUrlEncoded
    @POST("user/resetpassword")
    Observable<SimpleResponse> resetPasswordUser(@Header("X-API-KEY") String ApiKey, @Field("user_id") int userId, @Field("password") String password);

    @FormUrlEncoded
    @POST("company/resetpassword")
    Observable<SimpleResponse> resetPasswordCompany(@Header("X-API-KEY") String ApiKey, @Field("companyid") int companyid, @Field("password") String password);

    @GET("company/media/{id}")
    Observable<ProfileMediaResponse> getCompanyMediaList(@Header("X-API-KEY") String ApiKey, @Path("id") int id);

    @GET("company/map/{id}")
    Observable<CompanyLocationResponse> getCompanyLocation(@Header("X-API-KEY") String ApiKey, @Path("id") int id);

    @Multipart
    @POST("company/rfq")
    Observable<SimpleResponse> submitCompanyRFQ(
            @Header("X-API-KEY") String ApiKey, @Part("companyid") RequestBody companyId, @Part("name") RequestBody name
            , @Part("phone") RequestBody phone, @Part("email") RequestBody email, @Part("unit") RequestBody unit, @Part("qty") RequestBody qty, @Part("search_keywords") RequestBody keyword,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part image,@Part("sectorid") RequestBody sectorId,
            @Part("flag") RequestBody flag,@Part("senderId") RequestBody senderid);

    @GET("categories/sub/{id}")
    Observable<SubCategoryResponse> getSubcategoriesList(@Header("X-API-KEY") String ApiKey, @Path("id") int id);

    @GET("categories/sectors")
    Observable<SelectorResponse> getSelectors(@Header("X-API-KEY") String ApiKey);

    @GET("world/countries")
    Observable<CountryResponse> getCountries(@Header("X-API-KEY") String ApiKey);

    @GET("world/cities/{id}")
    Observable<CityResponse> getCity(@Header("X-API-KEY") String ApiKey, @Path("id") int id);

    @FormUrlEncoded
    @POST("main/search")
    Observable<MainPageResponse> searchCompanyList(@Header("X-API-KEY") String ApiKey,
                                                   @Field("search") String search, @Field("sector_id") String sector_id,
                                                   @Field("city_id") String city_id, @Field("country_id") String countryId,
                                                   @Field("page") int pageId, @Field("limit") int pageLimit,
                                                   @Field("companyid") String companyid);

    @GET("main/sccids")
    Observable<SccidResponse> getSCCIDS(@Header("X-API-KEY") String ApiKey);

    @GET("user/home/{id}")
    Observable<UserProfileResponse> getUserProfile(@Header("X-API-KEY") String ApiKey, @Path("id") int id);

    @GET("feeds/get/{id}/{flag}/{page}")
    Observable<UserProfileFeedResponse> getUserFeed(@Header("X-API-KEY") String ApiKey, @Path("id") int id, @Path("flag") int flag,
                                                    @Path("page") int page);

    @Multipart
    @POST("feeds/create")
    Observable<SimpleResponse> addUserFeed(@Header("X-API-KEY") String ApiKey, @Part("id") RequestBody userId, @Part("flag") RequestBody flag,
                                           @Part("description") RequestBody description, @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("feeds/delete")
    Observable<SimpleResponse> removeFeed(@Header("X-API-KEY") String ApiKey,@Field("feedid") int feedId);

    @GET("user/friends/{id}")
    Observable<RequestMsgResponse> getFriendsList(@Header("X-API-KEY") String ApiKey,@Path("id") int id);

    @GET("user/media/{id}")
    Observable<ProfileMediaResponse> getUserMedia(@Header("X-API-KEY") String ApiKey, @Path("id") int id);

    @GET("user/visitors/{id}")
    Observable<UserProfileVisitorResponse> getVisitors(@Header("X-API-KEY") String ApiKey,@Path("id") int id);

    @FormUrlEncoded
    @POST("media/video")
    Observable<SimpleResponse> uploadVideo(@Header("X-API-KEY") String ApiKey,
                                                   @Field("user_or_company_id") int id, @Field("search_keywords") String keyword,
                                                   @Field("flag") int flag, @Field("url") String url, @Field("is_private") int isPrivate);

    @Multipart
    @POST("media/image")
    Observable<SimpleResponse> uploadImage(@Header("X-API-KEY") String ApiKey, @Part("user_or_company_id") RequestBody userId, @Part("flag") RequestBody flag,
                                           @Part("search_keywords") RequestBody keyword,@Part("isprivate") RequestBody isprivate, @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST("oldjobs/delete")
    Observable<SimpleResponse> deleteJob(@Header("X-API-KEY") String ApiKey,@Field("job_id") int jobId );

    @FormUrlEncoded
    @POST("oldjobs/jobupdate")
    Observable<SimpleResponse> updateJob(@Header("X-API-KEY") String ApiKey,@Field("jobid") int jobId,@Field("jobtitle") String jobTitle,
                                         @Field("sectorid") int sectorid,@Field("description") String description,@Field("cityid") int cityId);

    @FormUrlEncoded
    @POST("oldjobs/jobcreate")
    Observable<SimpleResponse> createJob(@Header("X-API-KEY") String ApiKey,@Field("userid") int id,@Field("jobtitle") String jobTitle,
                                         @Field("sectorid") int sectorid,@Field("description") String description,@Field("cityid") int cityId);

    @GET("oldjobs/all/{id}")
    Observable<UserJobResponse> getJobs(@Header("X-API-KEY") String ApiKey, @Path("id") int id);

    @GET("media/delete/{media_id}")
    Observable<SimpleResponse> removeMedia(@Header("X-API-KEY") String ApiKey,@Path("media_id") Integer mediaId);

    @FormUrlEncoded
    @POST("user/sendrequest")
    Observable<SimpleResponse> sendRequest(@Header("X-API-KEY") String ApiKey,@Field("user_id") Integer fromId,@Field("user_contacts_id") Integer toUser);

    @FormUrlEncoded
    @POST("user/acceptfriend")
    Observable<SimpleResponse> acceptRequest(@Header("X-API-KEY") String ApiKey,@Field("user_id") Integer userid,@Field("user_contacts_id") Integer contactId);

    @GET("interest/all")
    Observable<InterestResponse> getAllInterest(@Header("X-API-KEY") String ApiKey);

    @FormUrlEncoded
    @POST("company/intersts")
    Observable<SimpleResponse> updateCompanyInterests(@Header("X-API-KEY") String ApiKey,@Field("companyid") Integer userid,
                                                      @Field("interest") String interest);

    @GET("company/jobs/{company_id}")
    Observable<CompanyJobResponse> getCompanyJobs(@Header("X-API-KEY") String ApiKey,@Path("company_id") Integer companyId);

    @GET("company/connections/{company_id}")
    Observable<ConnectionResponse> getCompanyConnections(@Header("X-API-KEY") String ApiKey,@Path("company_id") Integer companyId);

    @GET("company/rfqall/{company_id}")
    Observable<RFQResponse> getCompanyRFQ(@Header("X-API-KEY") String ApiKey,@Path("company_id") Integer companyId);

    @Multipart
    @POST("company/addtender")
    Observable<SimpleResponse> addPDF(@Header("X-API-KEY") String ApiKey, @Part("companyid") RequestBody companyId, @Part("name") RequestBody title
                                           , @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("company/deletetender")
    Observable<SimpleResponse> deleteTender(@Header("X-API-KEY") String ApiKey,@Field("tenderid") Integer tenderid);

    @GET("company/viewrfq/{rfq_id}")
    Observable<RFQDetailResponse> getRFQDetail(@Header("X-API-KEY") String ApiKey , @Path("rfq_id") Integer rfqId);

    @FormUrlEncoded
    @POST("company/jobs")
    Observable<SimpleResponse> createJob(@Header("X-API-KEY") String ApiKey,@Field("company_id") int companyid,
                                         @Field("city_id") int cityid,@Field("sector_id") int sectorid,
                                         @Field("title") String jobtitle,@Field("salary") String salary,
                                         @Field("description") String description,
                                         @Field("age") String age,@Field("no_vacancies") String vacancies,
                                         @Field("start_date") String startdate,@Field("end_date") String enddate);

    @FormUrlEncoded
    @POST("company/jobupdate")
    Observable<SimpleResponse> updateJob(@Header("X-API-KEY") String ApiKey,@Field("job_id") Integer jobID,
                                         @Field("city_id") int cityid,@Field("sector_id") int sectorid,
                                         @Field("title") String jobtitle,@Field("salary") String salary,
                                         @Field("description") String description,
                                         @Field("age") String age,@Field("no_vacancies") String vacancies,
                                         @Field("start_date") String startdate,@Field("end_date") String enddate);
    @FormUrlEncoded
    @POST("company/deletejob")
    Observable<SimpleResponse> deleteCompanyJob(@Header("X-API-KEY") String ApiKey,@Field("job_id") Integer jobID);

    @Multipart
    @POST("company/logo")
    Observable<SimpleResponse> uploadCompanyLogo(@Header("X-API-KEY") String ApiKey, @Part("companyid") RequestBody userId,
                                                 @Part MultipartBody.Part image,@Part MultipartBody.Part banner);

    @FormUrlEncoded
    @POST("interest/delete")
    Observable<SimpleResponse> deleteInterest(@Header("X-API-KEY") String ApiKey, @Field("id") Integer jobID);

    @Multipart
    @POST("user/logo")
    Observable<SimpleResponse> uploadUserLogo(@Header("X-API-KEY") String ApiKey, @Part("userid") RequestBody userId,
                                              @Part MultipartBody.Part image,@Part MultipartBody.Part bannerImage);


    @GET("user/hide/{user_id}/email/{status}")
    Observable<SimpleResponse> hideEmail(@Header("X-API-KEY") String ApiKey,@Path("user_id") Integer userId,
                                         @Path("status") int status);

    @GET("user/hide/{user_id}/phone/{status}")
    Observable<SimpleResponse> hidePhone(@Header("X-API-KEY") String ApiKey,@Path("user_id") Integer userId,
                                         @Path("status") int status);

    @GET("user/hide/{user_id}/school/{status}")
    Observable<SimpleResponse> hideSchool(@Header("X-API-KEY") String ApiKey,@Path("user_id") Integer userId,
                                         @Path("status") int status);

    @GET("user/hide/{user_id}/work/{status}")
    Observable<SimpleResponse> hideWork(@Header("X-API-KEY") String ApiKey,@Path("user_id") Integer userId,
                                         @Path("status") int status);

    @GET("user/hide/{user_id}/university/{status}")
    Observable<SimpleResponse> hideUniversity(@Header("X-API-KEY") String ApiKey,@Path("user_id") Integer userId,
                                         @Path("status") int status);

    @FormUrlEncoded
    @POST("company/interestdelete")
    Observable<SimpleResponse> deleteCompanyInterest(@Header("X-API-KEY") String ApiKey, @Field("id") Integer jobID);

    @FormUrlEncoded
    @POST("company/productremove")
    Observable<SimpleResponse> deleteCompanyProduct(@Header("X-API-KEY") String ApiKey, @Field("productid") Integer productId);

    @FormUrlEncoded
    @POST("user/interests")
    Observable<SimpleResponse> updateUserInterest(@Header("X-API-KEY") String ApiKey,@Field("userid") Integer userid,
                                                      @Field("interest") String interest);

    @FormUrlEncoded
    @POST("company/edit")
    Observable<SimpleResponse> updateCompanyProfile(@Header("X-API-KEY") String ApiKey,@Field("companyid") Integer companyId,
                                                  @Field("year") Integer year,@Field("website") String website,@Field("description") String description,
                                                    @Field("sectorid") Integer sectorid,@Field("cityid") Integer cityid,@Field("postcode") String postcode,
                                                    @Field("landline") String landline,
                                                    @Field("manufacturer") int manufacturer,
                                                    @Field("trader") int trader,
                                                    @Field("serviceCompany") int serviceCompany);

    @GET("colors/get")
    Observable<ColorResponse> getColors(@Header("X-API-KEY") String ApiKey);

    @Multipart
    @POST("company/addproduct")
    Observable<SimpleResponse> addProduct(@Header("X-API-KEY") String ApiKey, @Part("companyid") RequestBody companyId, @Part("title") RequestBody title
            , @Part("price") RequestBody price, @Part("description") RequestBody description, @Part("colors") RequestBody colors, @Part("size") RequestBody size,
                                          @Part MultipartBody.Part[] images);

    @GET("sizes/get")
    Observable<SizeResponse> getSize(@Header("X-API-KEY") String ApiKey);

    @FormUrlEncoded
    @POST("company/updateproduct")
    Observable<SimpleResponse> updateProduct(@Header("X-API-KEY") String ApiKey,@Field("productid") Integer productId,
                                             @Field("title") String title, @Field("price") String price, @Field("description") String description,
                                             @Field("colors") String colors,@Field("size") String size);

    @GET("company/product/{product_id}")
    Observable<ProductDetail> getProductDetail(@Header("X-API-KEY") String ApiKey, @Path("product_id") int id);

    @Multipart
    @POST("company/addimage")
    Observable<SimpleResponse> addProductImage(@Header("X-API-KEY") String ApiKey,@Part("productid") RequestBody productId,
                                              @Part MultipartBody.Part image);

    @Multipart
    @POST("company/editimage")
    Observable<SimpleResponse> updateProductImage(@Header("X-API-KEY") String ApiKey, @Part("imageid") RequestBody imageId,
                                              @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("jobs/all")
    Observable<JobResponse> getAllJobs(@Header("X-API-KEY") String ApiKey, @Field("currentpage") int currentPage,
                                       @Field("limit") int limit);

    @FormUrlEncoded
    @POST("jobs/companies")
    Observable<JobResponse> searchCompanyJobs(@Header("X-API-KEY") String ApiKey,
                                              @Field("search") String search,@Field("sector_id") String sectorId,
                                              @Field("city_id") String cityId,@Field("country_id") String countryId,
                                             @Field("currentpage") int currentPage, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("jobs/applicants")
    Observable<JobResponse> searchApplicantJobs(@Header("X-API-KEY") String ApiKey,
                                                @Field("search") String search,@Field("sector_id") String sectorId,
                                                @Field("city_id") String cityId,@Field("country_id") String countryId,
                                                @Field("currentpage") int currentPage, @Field("limit") int limit);

    @FormUrlEncoded
    @POST("user/update")
    Observable<SimpleResponse> updateUserProfile(@Header("X-API-KEY") String ApiKey,@Field("userid") int userId,@Field("email") String email,
                                                 @Field("phone") String phone,@Field("languages") String languages,
                                                 @Field("school") String school,@Field("university") String university,
                                                 @Field("work") String work, @Field("cityid") String cityId,@Field("countryid") String countryId);

    @GET("company/all")
    Observable<AllCompanyResponse> getAllCompanies(@Header("X-API-KEY") String ApiKey);

    @GET("media/all")
    Observable<ProfileMediaResponse> getAllMedias(@Header("X-API-KEY") String ApiKey);

    @FormUrlEncoded
    @POST("media/search")
    Observable<MediaSearchReponse> searchMedias(@Header("X-API-KEY") String ApiKey, @Field("image_or_video") int imageOrVideo,
                                                @Field("page") int currentPage, @Field("limit") int limit,
                                                @Field("search") String search, @Field("sector_id") String sectorId,
                                                @Field("interest_id") String interestID,
                                                @Field("city_id") String cityId, @Field("country_id") String countryId,
                                                @Field("company_id") String companyID,@Field("page") Integer page);

    @GET("jobs/view/{user_id}")
    Observable<ApplicantResponse> getApplicationDetail(@Header("X-API-KEY") String ApiKey, @Path("user_id") String id);

    @FormUrlEncoded
    @POST("company/connect")
    Observable<SimpleResponse> connectToCompany(@Header("X-API-KEY") String ApiKey,@Field("companyid") Integer companyid,
                                                @Field("connectwith") Integer connectwith);

    @FormUrlEncoded
    @POST("community/list")
    Observable<CommunityListResponse> getCommunityList(@Header("X-API-KEY") String ApiKey,@Field("page") int currentPage);

    @FormUrlEncoded
    @POST("community/search")
    Observable<CommunityListResponse> searchCommunityList(@Header("X-API-KEY") String ApiKey,@Field("search") String searchKey,
                                                          @Field("sector_id") Integer sectorID);


    @FormUrlEncoded
    @POST("community/details")
    Observable<CommunityDetailResponse> getCommunityDetail(@Header("X-API-KEY") String ApiKey,
                                                           @Field("community_id") Integer communityID,@Field("user_or_company_members_id") Integer userOrCompanyId,
                                                           @Field("flag") Integer flag);

    @FormUrlEncoded
    @POST("community/join")
    Observable<SimpleResponse> joinCommunity(@Header("X-API-KEY") String ApiKey,@Field("community_id") Integer communityID,
                                             @Field("user_or_company_members_id") Integer memberId,@Field("flag") Integer flag);

    @FormUrlEncoded
    @POST("community/leave")
    Observable<SimpleResponse> leaveCommunity(@Header("X-API-KEY") String ApiKey,@Field("community_id") Integer communityID,
                                             @Field("user_or_company_members_id") Integer memberId,@Field("flag") Integer flag);

    @FormUrlEncoded
    @POST("community/delete")
    Observable<SimpleResponse> deleteCommunity(@Header("X-API-KEY") String ApiKey,@Field("community_id") Integer communityID);

    @Multipart
    @POST("community/edit")
    Observable<SimpleResponse> editCommunity(@Header("X-API-KEY") String ApiKey,@Part("community_id") RequestBody communityID,
                                             @Part("user_or_company_id") RequestBody memberId,
                                             @Part("sector_id") RequestBody sectorID,
                                             @Part("title") RequestBody title,@Part("description") RequestBody description,
                                             @Part("flag") RequestBody flag,@Part("city_id") RequestBody cityId,
                                             @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("community/discussion/list")
    Observable<DiscussionListResponse> getDiscussionList(@Header("X-API-KEY") String ApiKey, @Field("page") Integer page);

    @FormUrlEncoded
    @POST("community/feed/list")
    Observable<CommunityFeedResponse> getCommunityFeedList(@Header("X-API-KEY") String ApiKey,@Field("page") int currentPage);

    @FormUrlEncoded
    @POST("community/feed/comment/create")
    Observable<SimpleResponse> writeFeedComment(@Header("X-API-KEY") String ApiKey,@Field("feed_id") int feedId,
                                                       @Field("user_or_company") int userOrCompany,@Field("comment") String comment,
                                                       @Field("flag") int flag);

    @FormUrlEncoded
    @POST("community/discussion/list")
    Observable<CommunityEventListResponse> getEventList(@Header("X-API-KEY") String ApiKey, @Field("page") Integer page);

    @Multipart
    @POST("community/create")
    Observable<SimpleResponse> createCommunity(@Header("X-API-KEY") String ApiKey,
                                               @Part("user_or_company_id") RequestBody memberId,
                                               @Part("sector_id") RequestBody sectorID,
                                               @Part("title") RequestBody title,@Part("description") RequestBody description,
                                               @Part("flag") RequestBody flag,@Part("city_id") RequestBody cityId,
                                               @Part MultipartBody.Part image);

    @Multipart
    @POST("community/feed/create")
    Observable<SimpleResponse> createNewsFeed(@Header("X-API-KEY") String ApiKey,
                                               @Part("user_or_company_id") RequestBody memberId,
                                               @Part("community_id") RequestBody communityId,
                                               @Part("title") RequestBody title,@Part("description") RequestBody description,
                                               @Part("flag") RequestBody flag,
                                               @Part MultipartBody.Part image);


    @FormUrlEncoded
    @POST("community/feed/details")
    Observable<CommunityFeedDetailResponse> getFeedDetail(@Header("X-API-KEY") String ApiKey, @Field("feed_id") int feedId);

    @FormUrlEncoded
    @POST("community/event/list")
    Observable<CommunityEventResponse> getCommunityEvents(@Header("X-API-KEY") String ApiKey,@Field("community_id") int communityId,
                                                          @Field("page") int page);

    @FormUrlEncoded
    @POST("community/feed/comments")
    Observable<FeedCommentResponse> getFeedComments(@Header("X-API-KEY") String ApiKey,@Field("feed_id") int feedId,
                                                    @Field("page") int page);

    @FormUrlEncoded
    @POST("community/discussion/create")
    Observable<SimpleResponse> createDiscussion(@Header("X-API-KEY") String ApiKey,@Field("community_id") int communityId,
                                                @Field("title") String title,@Field("description") String description,
                                                @Field("flag") int flag,@Field("user_or_company") int userOrCompany);

    @FormUrlEncoded
    @POST("community/discussion/reply/list")
    Observable<DiscussionCommentResponse> getDiscussionReplyList(@Header("X-API-KEY") String ApiKey, @Field("page") int page,@Field("discussion_id") int discussionID);

    @FormUrlEncoded
    @POST("community/discussion/reply/create")
    Observable<SimpleResponse> createDiscussionComment(@Header("X-API-KEY") String ApiKey,@Field("discussion_id") int discussionID,
                                                                  @Field("post") String post,@Field("user_or_company_id") int userOrCompany,
                                                                  @Field("flag") int flag);

    @FormUrlEncoded
    @POST("community/event/main")
    Observable<CommunityEventListResponse> getCommunityMainEvents(@Header("X-API-KEY") String ApiKey, @Field("page") int page);

    @FormUrlEncoded
    @POST("community/media/all/image")
    Observable<CommunityImageResponse> getCommunityImageList(@Header("X-API-KEY") String ApiKey, @Field("page") int page);

    @FormUrlEncoded
    @POST("community/media/all/video")
    Observable<CommunityImageResponse> getCommunityVideoList(@Header("X-API-KEY") String ApiKey, @Field("page") int page);

    @Multipart
    @POST("community/media/image")
    Observable<SimpleResponse> uploadCommunityImage(@Header("X-API-KEY") String ApiKey, @Part("user_or_company_id") RequestBody userId,
                                                    @Part("community_id") RequestBody communityId,
                                                    @Part("flag") RequestBody flag,
                                           @Part("search_keywords") RequestBody keyword, @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("media/video")
    Observable<SimpleResponse> uploadCommunityVideo(@Header("X-API-KEY") String ApiKey,
                                           @Field("user_or_company_id") int id,@Field("community_id") int communityId
                                          , @Field("search_keywords") String keyword,
                                           @Field("flag") int flag, @Field("url") String url, @Field("is_private") int isPrivate);

    @FormUrlEncoded
    @POST("community/members")
    Observable<CommunityMemberResponse> getCommunityMemberLists(@Header("X-API-KEY") String ApiKey,@Field("community_id") int communityId,
                                                                @Field("page") int page);

    @Multipart
    @POST("community/event/create")
    Observable<SimpleResponse> createEvents(@Header("X-API-KEY") String ApiKey,@Part("community_id") RequestBody communityId,
                                            @Part("title") RequestBody title,@Part("description") RequestBody description,
                                            @Part("city_id") RequestBody cityId,@Part("event_date") RequestBody event_date,
                                            @Part("flag") RequestBody flag,@Part("user_or_company") RequestBody userOrCompany,
                                            @Part MultipartBody.Part image);

    @Multipart
    @POST("community/event/edit")
    Observable<SimpleResponse> editEvents(@Header("X-API-KEY") String ApiKey,@Part("event_id") RequestBody eventId,
                                            @Part("title") RequestBody title,@Part("description") RequestBody description,
                                            @Part("city_id") RequestBody cityId,@Part("event_date") RequestBody event_date,
                                            @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("community/event/delete")
    Observable<SimpleResponse> deleteEvent(@Header("X-API-KEY") String ApiKey,@Field("event_id") int eventID);

    @FormUrlEncoded
    @POST("language")
    Observable<SimpleResponse> updateLanguage(@Header("X-API-KEY") String ApiKey, @Field("id") int id,
                                              @Field("flag") int flag,  @Field("language") String language);

    @FormUrlEncoded
    @POST("community/chat")
    Observable<SimpleResponse> chatInGroup(@Header("X-API-KEY") String ApiKey,@Field("community_id") int communityId ,  @Field("user_or_company_members_id") int loginId,
                                              @Field("flag") int flag,  @Field("message") String message);
}