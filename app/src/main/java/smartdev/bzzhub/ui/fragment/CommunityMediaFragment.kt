package smartdev.bzzhub.ui.fragment

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.widget.*
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import smartdev.bzzhub.BzzApp
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.*
import smartdev.bzzhub.ui.activity.MainActivity
import smartdev.bzzhub.ui.adapter.CommunityBubbleAdapter
import smartdev.bzzhub.ui.adapter.CommunityImageAdapter
import smartdev.bzzhub.ui.adapter.CommunityVideoAdapter
import smartdev.bzzhub.ui.base.BaseFragment
import smartdev.bzzhub.util.Constant
import smartdev.bzzhub.util.navigation.NavigationManager
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import smartdev.bzzhub.repository.model.CommunityImageResponse as CommunityImageResponse1

class CommunityMediaFragment :  BaseFragment(), CommunityBubbleAdapter.SelectedListener,CommunityVideoAdapter.SelectedListener,
CommunityImageAdapter.SelectedListener{

    var mContext: Context? = null
    var communityBubbleAdapter: CommunityBubbleAdapter? = null
    var titleList = ArrayList<String>()

    var recyclerViewTab: RecyclerView? = null
    var recyclerImage: RecyclerView? = null
    var recyclerVideo: RecyclerView? = null
    lateinit var tvFindCommunity: TextView
    lateinit var ivClose: ImageView

    private var selectorList = ArrayList<SelectorResponse.Result>()
    private var countries = ArrayList<CountryResponse.Result>()
    private var cities = ArrayList<CityResponse.Result>()
    private var allCities = ArrayList<CityResponse.Result>()
    private var cityAdapter: ArrayAdapter<CityResponse.Result>? = null

    private var imagePageId = 1
    private var videoPageId = 1
    private var isImageLoading = false
    private var isVideoLoading = false

    private var loadMoreImage = true
    private var loadMoreVideo = true

    private var imageList = ArrayList<CommunityImageResponse1.Result>()
    private var videoList = ArrayList<CommunityImageResponse1.Result>()

    private var imageAdapter: CommunityImageAdapter? = null
    private var communityVideoAdapter: CommunityVideoAdapter? = null

    private var layoutAddVideo: View ? = null
    private var layoutAddImage: View? = null

    private var strKeyword = ""
    private var tvSelectFile: TextView? = null
    private var file: File? = null
    private var communityList = ArrayList<CommunityListResponse.Result>()
    private var groupId :Int = 0
    private var strUrl = ""
    var isPrivate = 0

    private lateinit var easyImage: EasyImage
    private var imageRequestCode = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community_media, container, false)

        // Define Easy Image
        easyImage =
                EasyImage.Builder(activity!!).setCopyImagesToPublicGalleryFolder(false)
                        .allowMultiple(false)
                        .build()

        tvFindCommunity = view.findViewById(R.id.tv_find_company)
        recyclerViewTab = view.findViewById(R.id.recyclerView_tab)
        ivClose = view.findViewById(R.id.iv_close)
        recyclerImage = view.findViewById(R.id.recyclerView_image)
        recyclerVideo = view.findViewById(R.id.recyclerView_video)
        // layoutAdd = view.findViewById(R.id.layout_add)

        allCities = Constant.getInstance().cities
        countries = Constant.getInstance().countries
        selectorList = Constant.getInstance().selectorList
        communityList = Constant.getInstance().communityList

        layoutAddVideo = view.findViewById(R.id.layout_add_video)
        layoutAddImage = view.findViewById(R.id.layout_add_image)

        initRecyclerViews()
        initTitle()

        getImageList()
        getVideoList()

        if (Constant.getInstance().loginFlag == 3){
            layoutAddVideo?.visibility = GONE
            layoutAddImage?.visibility = GONE
        }
        layoutAddImage?.setOnClickListener {
            showAddImageDialog()
        }

        layoutAddVideo?.setOnClickListener {
            showAddVideoDialog()
        }
        return view
    }


    fun initRecyclerViews() {
        recyclerViewTab!!.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        recyclerImage!!.layoutManager = GridLayoutManager(mContext,2)
        recyclerVideo!!.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

        recyclerImage!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layotuManager = recyclerView.layoutManager as GridLayoutManager?
                if (!isImageLoading) {
                    if (layotuManager != null && layotuManager.findLastCompletelyVisibleItemPosition() == imageList.size - 1) {
                        //bottom of list!
                        //      searchCompanyList()
                        isImageLoading = true
                        loadMoreImage = true
                    }
                }
            }
        })

        recyclerVideo!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isVideoLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == videoList.size - 1) {
                        //bottom of list!
                        //      searchCompanyList()
                        isVideoLoading = true
                        loadMoreVideo = true
                    }
                }
            }
        })
    }


    private fun initTitle() {
        titleList.add(activity!!.resources.getString(R.string.str_news_feed))
        titleList.add(activity!!.resources.getString(R.string.str_discussions))
        titleList.add(activity!!.resources.getString(R.string.str_media))
        titleList.add(activity!!.resources.getString(R.string.str_events))
        titleList.add(activity!!.resources.getString(R.string.str_community))

        communityBubbleAdapter = CommunityBubbleAdapter(context!!, titleList)
        communityBubbleAdapter!!.selected = 2
        recyclerViewTab!!.adapter = communityBubbleAdapter
        communityBubbleAdapter!!.listener = this@CommunityMediaFragment
    }

    override fun onBubbleSelected(position: Int) {
        when (position) {
            0 -> {
                (activity!! as MainActivity).addFragment(CommunityFeedFragment(), false,
                        false, R.id.frameLayout, null)
            }
            1 -> {
                (activity!! as MainActivity).addFragment(CommunityDiscussionFragment(), false,
                        false, R.id.frameLayout, null)
            }
            2 -> {
                //  (activity!! as MainActivity).addFragment(CommunityDiscussionFragment(),false,
                //         false,R.id.frameLayout,null)
            }
            3 -> {
                (activity!! as MainActivity).addFragment(CommunityEventFragment(), false,
                        false, R.id.frameLayout, null)
            }
            4 -> {
                (activity!! as MainActivity).addFragment(CommunityFragment(), false,
                        false, R.id.frameLayout, null)
            }
        }
    }

    fun getImageList() {
        BzzApp.getBzHubRepository().getCommunityImages(imagePageId).
        subscribe(object : Observer<CommunityImageResponse1> {
            var mDispose: Disposable? = null
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                mDispose = d
            }


            override fun onNext(t: CommunityImageResponse1) {
                isImageLoading = t.result == null || t.result.size != 10

                imageList .clear()
                if (t.result != null) {
                    imageList = t.result
                    if (loadMoreImage) {
                        loadMoreImage = false
                        if (loadMoreImage && !isImageLoading){
                            imagePageId += 1
                        }
                    }
                }

                if (imageAdapter == null){
                    Log.d("imageCalled",imageList.size.toString())

                    imageAdapter = CommunityImageAdapter(activity!!, imageList,this@CommunityMediaFragment)
             //      imageAdapter!!.listener = this@CommunityMediaFragment
                    recyclerImage!!.adapter = imageAdapter
                }else{
                    Log.d("imageCalled","bbbb")
                    imageAdapter!!.addItems(imageList)
                    recyclerImage!!.adapter = imageAdapter
                }
            }

            override fun onError(e: Throwable) {
            }
        })

    }

    fun getVideoList(){
        BzzApp.getBzHubRepository().getCommunityVideos(videoPageId).
        subscribe(object : Observer<CommunityImageResponse1> {
            var mDispose: Disposable? = null
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
                mDispose = d
            }


            override fun onNext(t: CommunityImageResponse1) {
                isVideoLoading = t.result == null || t.result.size != 10

                videoList .clear()
                if (t.result != null) {
                    videoList = t.result
                    if (loadMoreVideo) {
                        loadMoreVideo = false
                        if (loadMoreVideo && !isVideoLoading){
                            videoPageId += 1
                        }
                    }
                }

                if (communityVideoAdapter == null){
                    communityVideoAdapter = CommunityVideoAdapter(activity!!, t.result!!,this@CommunityMediaFragment)
                    //       imageAdapter!!.listener = this@CommunityMediaFragment
                    recyclerVideo!!.adapter = communityVideoAdapter
                    Log.d("videoSize11", t.result!!.size.toString())
                }else{
                    communityVideoAdapter!!.addItems(videoList)
                    recyclerVideo!!.adapter = communityVideoAdapter
                    Log.d("videoSize22", videoList.size.toString())
                }
            }

            override fun onError(e: Throwable) {
            }
        })
    }

    override fun onVideoSelected(URL: String?) {
        val bundle = Bundle()
        bundle.putString(smartdev.bzzhub.util.navigation.Arg.ARG_URL_VIDEO, URL)
       NavigationManager.gotoVideoActivity(currentActivity!!,bundle)
    }

    override fun onImageSelected(imageURL: String?) {
        val bundle = Bundle()
        bundle.putString(smartdev.bzzhub.util.navigation.Arg.ARG_URL_IMAGE, imageURL)
        NavigationManager.gotoImageActivity(currentActivity!!,bundle)
    }

    private fun showAddImageDialog() {
        val dialog = Dialog(currentActivity)
        val layoutInflater = LayoutInflater.from(currentActivity)
        val view = layoutInflater.inflate(R.layout.dialog_add_community_photo, null)
        dialog.window
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        Objects.requireNonNull(dialog.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        strKeyword = ""
        val editText = dialog.findViewById<EditText>(R.id.edittext_description)
        val layoutSelectFile = dialog.findViewById<RelativeLayout>(R.id.layout_select_file)
        layoutSelectFile.setOnClickListener { v: View? ->
            val permissions = RxPermissions(this)
            val subscribe = permissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe { granted: Boolean ->
                        if (granted) {
                            imageRequestCode = 100
                            easyImage.openChooser(this)
                        }
                    }
        }

        val spinnerGroup = view.findViewById<Spinner>(R.id.spinner_group)
        val groupSpinnerAdapter: ArrayAdapter<CommunityListResponse.Result> = ArrayAdapter<CommunityListResponse.Result>(currentActivity, android.R.layout.simple_spinner_dropdown_item, communityList)
        groupSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGroup.adapter = groupSpinnerAdapter
        spinnerGroup.setSelection(communityList.size - 1)


        spinnerGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                groupId = communityList[position].communityId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        tvSelectFile = dialog.findViewById<TextView>(R.id.tv_select_file)
        val tvUpload = dialog.findViewById<TextView>(R.id.tv_upload)
        tvUpload.setOnClickListener { v: View? ->
            strKeyword = editText.text.toString().trim { it <= ' ' }
            when {
                strKeyword.isEmpty() -> {
                    (currentActivity as MainActivity).onError(Exception(currentActivity.resources.getString(R.string.str_empty_keyword)))
                }
                file == null -> {
                    (currentActivity as MainActivity).onError(Exception(currentActivity.resources.getString(R.string.str_empty_file)))
                }
                else -> {
                    addImage(strKeyword, 1, 0, file!!)
                    dialog.dismiss()
                }
            }
        }
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }

    private fun addImage(strKeyword: String, flag: Int, isPrivate: Int, file: File) {
        BzzApp.getBzHubRepository().uploadCommunityImage(groupId.toString(),Constant.getInstance().loginID.toString(),strKeyword,Constant.getInstance().loginFlag,file!!)
                .subscribe(object : Observer<SimpleResponse> {
                    var mDispose: Disposable? = null
                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(simpleResponse: SimpleResponse) {
                        if (simpleResponse.status && simpleResponse.code == 200) {
                            imagePageId = 1
                           getImageList()
                        } else {
                            (currentActivity as MainActivity).onError(java.lang.Exception(simpleResponse.message))
                        }
                    }

                    override fun onError(e: Throwable) {
                        (currentActivity as MainActivity).onError(e)
                    }

                    override fun onComplete() {
                        mDispose!!.dispose()
                    }
                })
    }

    private fun showAddVideoDialog() {
        val dialog = Dialog(currentActivity)
        val layoutInflater = LayoutInflater.from(currentActivity)
        val view = layoutInflater.inflate(R.layout.dialog_add_community_video, null)
        dialog.window
        strKeyword = ""
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        Objects.requireNonNull(dialog.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val editTextKeyword = dialog.findViewById<EditText>(R.id.edittext_keyword)
        val checkboxPublic: AppCompatCheckBox = dialog.findViewById(R.id.checkbox_public)
        val checkBoxYoutube: AppCompatCheckBox = dialog.findViewById(R.id.checkbox_youtube)
        checkboxPublic.setOnCheckedChangeListener { _, isChecked -> isPrivate = if (isChecked) 0 else 1 }
        val url = dialog.findViewById<EditText>(R.id.edittext_url)
        val tvAdd = dialog.findViewById<TextView>(R.id.tv_add)

        val spinnerGroup = view.findViewById<Spinner>(R.id.spinner_group)
        val groupSpinnerAdapter: ArrayAdapter<CommunityListResponse.Result> = ArrayAdapter<CommunityListResponse.Result>(currentActivity, android.R.layout.simple_spinner_dropdown_item, communityList)
        groupSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGroup.adapter = groupSpinnerAdapter
        spinnerGroup.setSelection(communityList.size - 1)

        spinnerGroup.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                groupId = communityList[position].communityId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        tvAdd.setOnClickListener { v: View? ->
            strKeyword = editTextKeyword.text.toString().trim { it <= ' ' }
            strUrl = url.text.toString().trim { it <= ' ' }
            if (strKeyword.isEmpty()) {
                (currentActivity as MainActivity).onError(java.lang.Exception(currentActivity.resources.getString(R.string.str_empty_keyword)))
            } else if (strUrl.isEmpty()) {
                (currentActivity as MainActivity).onError(java.lang.Exception(currentActivity.resources.getString(R.string.str_empty_URL)))
            } else {
                addVideo(isPrivate)
                dialog.dismiss()
            }
        }
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window.attributes = lp
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()
    }

    private fun addVideo(isPrivate: Int) {
        BzzApp.getBzHubRepository().uploadCommunityVideo(Constant.getInstance().loginID,groupId,strKeyword,Constant.getInstance().loginFlag,strUrl,isPrivate)
                .subscribe(object : Observer<SimpleResponse> {
                    var mDispose: Disposable? = null
                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(simpleResponse: SimpleResponse) {
                        if (simpleResponse.code == 200) {
                            videoPageId = 1
                            getVideoList()
                        } else {
                            (currentActivity as MainActivity).onError(java.lang.Exception(simpleResponse.message))
                        }
                    }

                    override fun onError(e: Throwable) {
                        (currentActivity as MainActivity).onError(e)
                    }

                    override fun onComplete() {
                        mDispose!!.dispose()
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Profile Image Changed
        easyImage.handleActivityResult(requestCode, resultCode, data, activity!!, object :
                DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {

                if (imageRequestCode == 100) {
                    file = imageFiles.first().file
                    if (tvSelectFile != null) tvSelectFile!!.text = file!!.name
                }
            }
        })

    }


}
