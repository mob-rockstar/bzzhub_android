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
import android.view.View.VISIBLE
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tbruyelle.rxpermissions2.RxPermissionsFragment
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import smartdev.bzzhub.BzzApp
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.*
import smartdev.bzzhub.ui.activity.CommunityDetailActivity
import smartdev.bzzhub.ui.activity.MainActivity
import smartdev.bzzhub.ui.adapter.CommunityAdapter
import smartdev.bzzhub.ui.adapter.CommunityBubbleAdapter
import smartdev.bzzhub.ui.base.BaseFragment
import smartdev.bzzhub.util.Constant
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class CommunityFragment : BaseFragment() ,CommunityAdapter.SelectedListener,CommunityBubbleAdapter.SelectedListener {

    var mContext: Context? = null
    var communityBubbleAdapter : CommunityBubbleAdapter? = null
    var titleList =   ArrayList<String>()

    var recyclerViewTab : RecyclerView? = null
    var recyclerView: RecyclerView? = null
    lateinit var tvFindCommunity : TextView
    lateinit var ivClose: ImageView

    lateinit var spinnerCity: Spinner
    lateinit var  layoutSearchBar: ConstraintLayout

    private var searchKey: String? = null
    private var selectorId: Int? = null
    private var cityId: Int? = null
    private var countryId: Int? = null
    private var companyId: Int? = null
    private var description: String = ""
    private  var spinnerSubcategory:Spinner? = null
    private var selectorList = ArrayList<SelectorResponse.Result>()
    private var countries = ArrayList<CountryResponse.Result>()
    private var cities = ArrayList<CityResponse.Result>()
    private var allCities = ArrayList<CityResponse.Result>()
    private var cityAdapter: ArrayAdapter<CityResponse.Result>? = null
    private var pageId = 1
    private var communityAdapter: CommunityAdapter? = null
    private var isLoading = false
    private var loadMore = true
    private var communityList = ArrayList<CommunityListResponse.Result>()
    private var layoutAdd :View? = null
    private var interests: ArrayList<InterestResponse.Result> = ArrayList<InterestResponse.Result>()
    private var interestAdapter: ArrayAdapter<InterestResponse.Result>? = null
    private var interestID: Int = 0
    private var allCompanies = java.util.ArrayList<AllCompanyResponse.Result>()
    private var imageGroup : File? = null
    private var edittextFileName : EditText ? = null
    private var title: String = ""
    var subcategories = java.util.ArrayList<SubCategoryResponse.Result>()
    var subcategoryId :Int = 0

    var isSearch: Boolean = false

    private lateinit var easyImage: EasyImage
    private var file: File? = null
    private var imageRequestCode = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community, container, false)

        // Define Easy Image
        easyImage =
                EasyImage.Builder(activity!!).setCopyImagesToPublicGalleryFolder(false)
                        .allowMultiple(false)
                        .build()

        tvFindCommunity = view.findViewById(R.id.tv_find_company)
        recyclerViewTab = view.findViewById(R.id.recyclerView_tab)
        ivClose = view.findViewById(R.id.iv_close)
        recyclerView = view.findViewById(R.id.recyclerView)
        layoutAdd = view.findViewById(R.id.layout_add)

        allCities = Constant.getInstance().cities
        countries = Constant.getInstance().countries
        selectorList = Constant.getInstance().selectorList

        initRecyclerViews()
        initTitle()

        this.layoutSearchBar = view.findViewById(R.id.layout_search_bar)
        this.layoutSearchBar.setOnClickListener {
            openSearchCommunityDialog()
        }
  //      getAllCompanies()

        getAllInterest()
        ivClose.setOnClickListener {
            communityList.clear()
            communityAdapter!!.setItems(communityList)
            pageId = 1
            getCommunityList()
        }

        if (Constant.getInstance().loginFlag == 3){
            layoutAdd!!.visibility = GONE
        }else{
            layoutAdd!!.visibility = VISIBLE
        }

        layoutAdd!!.setOnClickListener {
            openAddCommunityDialog()
        }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onResume() {
        super.onResume()
        pageId = 1
         isLoading = false
         loadMore = true
        communityList.clear()
        communityAdapter = null
        getCommunityList()
    }

    fun initRecyclerViews(){
        recyclerViewTab!!.layoutManager = LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false)
        recyclerView!!.layoutManager = LinearLayoutManager(mContext)

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == communityList.size - 1) {
                        //bottom of list!
                        if (isSearch){
                            searchCommunities()
                        }else{
                            getCommunityList()
                        }

                        isLoading = true
                        loadMore = true
                    }
                }
            }
        })
    }

    private fun initTitle(){
        titleList.add(activity!!.resources.getString(R.string.str_news_feed))
        titleList.add(activity!!.resources.getString(R.string.str_discussions))
        titleList.add(activity!!.resources.getString(R.string.str_media))
        titleList.add(activity!!.resources.getString(R.string.str_events))
        titleList.add(activity!!.resources.getString(R.string.str_community))

        communityBubbleAdapter = CommunityBubbleAdapter(context!!,titleList)
        recyclerViewTab!!.adapter = communityBubbleAdapter
        communityBubbleAdapter!!.listener = this@CommunityFragment
    }

    private fun openSearchCommunityDialog(){
        val dialog = Dialog(currentActivity)
        val layoutInflater = LayoutInflater.from(currentActivity)

        val view = layoutInflater.inflate(R.layout.dialog_search_community, null)
        dialog.window

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        Objects.requireNonNull(dialog.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val layoutMain = view.findViewById<LinearLayout>(R.id.layout_main)
        layoutMain.setOnClickListener { dialog.dismiss() }
        val spinnerSector = view.findViewById<Spinner>(R.id.spinner_sector)
        val layoutSector = view.findViewById<RelativeLayout>(R.id.layout_sector)
        val layoutMembers = view.findViewById<LinearLayout>(R.id.layout_member_spinners)
        val tvSector = view.findViewById<TextView>(R.id.tv_sector)

        val tabLayout = view.findViewById<TabLayout> (R.id.tabLayout)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position == 0){
                    layoutMembers.visibility = GONE
                    tvSector.visibility = VISIBLE
                    layoutSector.visibility = VISIBLE
                    tvFindCommunity.text = activity!!.resources.getString(R.string.str_search_member)
                }else if (tab!!.position == 1){
                    layoutMembers.visibility = VISIBLE
                    tvSector.visibility = GONE
                    layoutSector.visibility = GONE
                    tvFindCommunity.text = activity!!.resources.getString(R.string.str_search_member)
                }
            }
        })

/*        val spinnerCompanyy = view.findViewById<Spinner>(R.id.spinner_company)

        val companyAdapter = ArrayAdapter(currentActivity, android.R.layout.simple_spinner_dropdown_item, allCompanies)
        companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCompanyy.adapter = companyAdapter
        spinnerCompanyy.setSelection(allCompanies.size - 1)

        spinnerCompanyy.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                companyId = allCompanies[position].companyId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }*/
        val editTextKeyword = view.findViewById<EditText>(R.id.editttext_key_word)
        if (ivClose.visibility == View.VISIBLE) {
            editTextKeyword.setText(tvFindCommunity!!.text.toString().trim { it <= ' ' })
        }

        val adapter: ArrayAdapter<SelectorResponse.Result> = ArrayAdapter<SelectorResponse.Result>(currentActivity, android.R.layout.simple_spinner_dropdown_item, selectorList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSector.adapter = adapter
        spinnerSector.setSelection(selectorList.size - 1)

        spinnerSector.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                selectorId = selectorList[position].categoryId

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val spinnerCountry = view.findViewById<Spinner>(R.id.spinner_country)
        val countrySpinnerAdapter: ArrayAdapter<CountryResponse.Result> = ArrayAdapter<CountryResponse.Result>(currentActivity, android.R.layout.simple_spinner_dropdown_item, countries)
        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = countrySpinnerAdapter
        spinnerCountry.setSelection(countries.size - 1)

        val spinnerInterest = view.findViewById<Spinner>(R.id.spinner_interest)
        interestAdapter = ArrayAdapter(currentActivity, android.R.layout.simple_spinner_dropdown_item, interests)
        interestAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerInterest.adapter = interestAdapter
        spinnerInterest.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                interestID = interests[position].interestId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerCity = view.findViewById<Spinner>(R.id.spinner_city)
        spinnerCountry.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                countryId = countries[position].countryId
                if (position != countries.size - 1) {
                    filterCities(countries[position].countryId)
                    setCityAdapter()
                } else {
                    cities = java.util.ArrayList<CityResponse.Result>()
                    setCityAdapter()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val tvSearchCompany = view.findViewById<TextView>(R.id.tv_search)
        tvSearchCompany.setOnClickListener {
            searchKey = editTextKeyword.text.toString().trim { it <= ' ' }
            countryId = countries[spinnerCountry.selectedItemPosition].countryId
            cityId = cities[spinnerCity.selectedItemPosition].cityId
            selectorId = selectorList[spinnerSector.selectedItemPosition].categoryId
            pageId = 1
         //   getCommunityList()
            /*
            mainPageAdapter.setProducts(java.util.ArrayList<Company>())
            companies = java.util.ArrayList<Company>()
            searchCompanyList()*/
            communityList .clear()
            searchCommunities()
            tvFindCommunity!!.text = if (searchKey!!.isNotEmpty()) searchKey else currentActivity.resources.getText(R.string.str_find_company)
            if (searchKey!!.isNotEmpty()) {
                ivClose.visibility = View.VISIBLE
            } else {
                ivClose.visibility = View.GONE
            }
            dialog.dismiss()
        }

        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes = lp
        dialog.setCanceledOnTouchOutside(true)

        dialog.setCancelable(true)
        dialog.show()
    }

    private fun filterCities(countryId: Int) {
        cities = java.util.ArrayList()
        if (allCities != null && !allCities.isEmpty()) {
            for (city in allCities) {
                if (city.countryId == countryId) {
                    cities.add(city)
                }
            }
        }
    }

    private fun setCityAdapter() {
        val result = CityResponse.Result(0, 0, "- Select -")
        cities.add(result)
        cityAdapter = ArrayAdapter(currentActivity, android.R.layout.simple_spinner_dropdown_item, cities)
        cityAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.adapter = cityAdapter
        spinnerCity.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                cityId = cities[position].cityId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun getCommunityList(){
        isSearch = false
        BzzApp.getBzHubRepository().getCommunities(pageId)
                .subscribe(object : Observer<CommunityListResponse>{
                    lateinit var mDisposable: Disposable
                    override fun onComplete() {
                        mDisposable.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposable = d
                    }

                    override fun onNext(t: CommunityListResponse) {
                        isLoading = t.result == null || t.result.size != 15

                        if (Constant.getInstance().communityList.isEmpty()){
                            Constant.getInstance().communityList = t.result
                        }
                        communityList = ArrayList()
                        if (t.result != null) {
                            communityList = t.result
                            if (loadMore) {
                                loadMore = false
                            }
                            pageId += 1
                        }

                        if (communityAdapter == null){
                            communityAdapter = CommunityAdapter(activity!!, t.result)
                            communityAdapter!!.listener = this@CommunityFragment
                            recyclerView!!.adapter = communityAdapter
                        }else{
                            communityAdapter!!.addCommunities(communityList)
                        }

                    }

                    override fun onError(e: Throwable) {
                        this@CommunityFragment.onError(e)
                    }
                })
    }

    fun searchCommunities(){
        isSearch  = true
        BzzApp.getBzHubRepository().searchCommunities(searchKey,selectorId!!)
                .subscribe(object :Observer<CommunityListResponse>{
                    lateinit var mDisposable: Disposable
                    override fun onComplete() {
                        mDisposable.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposable = d
                    }

                    override fun onNext(t: CommunityListResponse) {
                        isLoading = t.result == null || t.result.size != 15

                        if (Constant.getInstance().communityList.isEmpty()){
                            Constant.getInstance().communityList = t.result
                        }
                        communityList .clear()
                        if (t.result != null) {
                            communityList = t.result
                            if (loadMore) {
                                loadMore = false
                                pageId += 1
                            }
                        }

                        if (communityAdapter == null){
                            communityAdapter = CommunityAdapter(activity!!, t.result)
                            communityAdapter!!.listener = this@CommunityFragment
                            recyclerView!!.adapter = communityAdapter
                        }else{
                            communityAdapter!!.addCommunities(communityList)
                        }
                    }

                    override fun onError(e: Throwable) {
                        this@CommunityFragment.onError(e)
                    }
                })
    }

    override fun onSelected(communityID: Int) {
        Log.d("loginFlags",Constant.getInstance().loginFlag.toString())
      //  if (Constant.getInstance().loginFlag != 3){
            var intent = Intent(activity!!,CommunityDetailActivity::class.java)
            intent.putExtra("community_id",communityID)
            startActivity(intent)
    //    }

    }

    override fun onBubbleSelected(position: Int) {
        when (position) {
            0 -> {
                   (activity!! as MainActivity).addFragment(CommunityFeedFragment(),false,
                                        false,R.id.frameLayout,null)
            }
            1 -> {
                (activity!! as MainActivity).addFragment(CommunityDiscussionFragment(),false,
                        false,R.id.frameLayout,null)
            }
            2 -> {
                  (activity!! as MainActivity).addFragment(CommunityMediaFragment(),false,
                         false,R.id.frameLayout,null)
            }
            3 -> {
                (activity!! as MainActivity).addFragment(CommunityEventFragment(),false,
                        false,R.id.frameLayout,null)
            }
        }
    }

    private fun getAllInterest() {
        interests = java.util.ArrayList()
        interests.add(InterestResponse.Result(0, "- Select -"))
        BzzApp.getBzHubRepository().interestList.subscribe(
                object : Observer<InterestResponse> {
                    var mDispose: Disposable? = null
                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(interestResponse: InterestResponse) {
                        if (interestResponse.status && interestResponse.code == 200) {
                            interests.addAll(interestResponse.result)
                        }
                    }

                    override fun onError(e: Throwable) {
                        this@CommunityFragment.onError(e)
                    }

                    override fun onComplete() {
                        mDispose!!.dispose()
                    }
                }
        )
    }

    private fun getAllCompanies() {
        BzzApp.getBzHubRepository().allCompanyList.subscribe(
                object : Observer<AllCompanyResponse> {
                    var mDispose: Disposable? = null
                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(allCompanyResponse: AllCompanyResponse) {
                        allCompanies = allCompanyResponse.result as java.util.ArrayList<AllCompanyResponse.Result>
                        allCompanies.add(AllCompanyResponse.Result(0, "- Select -"))
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        mDispose!!.dispose()
                    }
                }
        )
    }


    private fun openAddCommunityDialog() {
        val dialog = Dialog(currentActivity)
        val layoutInflater = LayoutInflater.from(currentActivity)

        val view = layoutInflater.inflate(R.layout.dialog_add_group, null)
        dialog.window

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        Objects.requireNonNull(dialog.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val spinnerCountry = view.findViewById<Spinner>(R.id.spinner_country)
        val countrySpinnerAdapter: ArrayAdapter<CountryResponse.Result> = ArrayAdapter<CountryResponse.Result>(currentActivity, android.R.layout.simple_spinner_dropdown_item, countries)
        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = countrySpinnerAdapter
        spinnerCountry.setSelection(countries.size - 1)

        spinnerCity = view.findViewById<Spinner>(R.id.spinner_city)
        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                countryId = countries[position].countryId
                if (position != countries.size - 1) {
                    filterCities(countries[position].countryId)
                    setCityAdapter()
                } else {
                    cities = java.util.ArrayList<CityResponse.Result>()
                    setCityAdapter()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        val spinnerSector = view.findViewById<Spinner>(R.id.spinner_sector)
        val adapter: ArrayAdapter<SelectorResponse.Result> = ArrayAdapter<SelectorResponse.Result>(currentActivity, android.R.layout.simple_spinner_dropdown_item, selectorList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSector.adapter = adapter
        spinnerSector.setSelection(selectorList.size - 1)

        spinnerSector.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                selectorId = selectorList[position].categoryId
                if (selectorId != 0) {
                    try {
                        getSubcategoriesList(selectorId!!)
                    } catch (e: java.lang.Exception) {
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        (view.findViewById<RelativeLayout>(R.id.layout_select_file)) .setOnClickListener {
            imageRequestCode = 100

                RxPermissions(this).request(
                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { granted ->
                            if (granted) {
                                easyImage.openChooser(this)
                            }
                        }

        }

        spinnerSubcategory = dialog.findViewById(R.id.spinner_category)
        edittextFileName = view.findViewById(R.id.edittext_file_name)

        edittextFileName!!.setOnClickListener {
            imageRequestCode = 100
            easyImage.openChooser(this)
        }
        val edittextTitle = view.findViewById<EditText>(R.id.edittext_title)
        val edittextDescription = view.findViewById<EditText>(R.id.edittext_description)
        var tvAdd = view.findViewById<TextView>(R.id.tv_add)
        tvAdd.setOnClickListener {
            title = edittextTitle.text.toString().trim()
            description = edittextDescription.text.toString().trim()
            if (cityId == null || subcategoryId ==  null || subcategoryId == 0){
                Toast.makeText(currentActivity, resources.getString(R.string.str_make_sure_valid_field), Toast.LENGTH_LONG).show()
            }else{

                addCommunity(title,selectorId!!,description,cityId!!,dialog)
            }
        }
        val lp = WindowManager.LayoutParams()
        val window = dialog.window
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes = lp
        dialog.setCanceledOnTouchOutside(true)

        dialog.setCancelable(true)
        dialog.show()
    }


    private fun getSubcategoriesList(id: Int) {
        BzzApp.getBzHubRepository().getSubcategories(id).subscribe(
                object : Observer<SubCategoryResponse> {
                    var mDisposable: Disposable? = null
                    override fun onSubscribe(d: Disposable) {
                        mDisposable = d
                    }

                    override fun onNext(subCategoryResponse: SubCategoryResponse) {
                        if (subCategoryResponse.status && subCategoryResponse.code == 200 && subCategoryResponse.result != null) {
                            subcategories = subCategoryResponse.result as java.util.ArrayList<SubCategoryResponse.Result>
                            subcategories.add(SubCategoryResponse.Result(0, 0, resources.getString(R.string.str_subcategory), "", 0))
                            val adapter = ArrayAdapter(currentActivity, android.R.layout.simple_spinner_dropdown_item, subcategories)
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerSubcategory!!.adapter = adapter
                            spinnerSubcategory!!.onItemSelectedListener = object : OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                                    subcategoryId = subcategories[position].subId
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {}
                            }
                        }
                    }

                    override fun onError(e: Throwable) {}
                    override fun onComplete() {
                        mDisposable!!.dispose()
                    }
                }
        )
    }


    fun addCommunity(title: String,sectorID: Int,description:String,cityID:Int,dialog: Dialog){
        if (title.isNotEmpty() && description.isNotEmpty() && imageGroup != null){
            dialog.dismiss()
            BzzApp.getBzHubRepository().createCommunity(subcategoryId,Constant.getInstance().loginID,title,description,
                    Constant.getInstance().loginFlag,cityID,imageGroup).subscribe(
                    object :Observer<SimpleResponse>{
                        lateinit var  mDispose:Disposable
                        override fun onComplete() {
                            mDispose.dispose()
                        }

                        override fun onSubscribe(d: Disposable) {
                            mDispose = d
                        }

                        override fun onNext(t: SimpleResponse) {
                            if (t.status && t.code == 200 ){
                                pageId = 1
                                getCommunityList()
                            }else{
                                this@CommunityFragment.onError(Exception(t.message))
                            }
                        }

                        override fun onError(e: Throwable) {
                            this@CommunityFragment.onError(e)
                        }
                    }
            )
        }else{
            Toast.makeText(currentActivity, resources.getString(R.string.str_make_sure_valid_field), Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Profile Image Changed
        easyImage.handleActivityResult(requestCode, resultCode, data, activity!!, object :
                DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {

                if (imageRequestCode == 100) {
                    imageGroup = imageFiles.first().file
                    edittextFileName!!.setText(imageGroup!!.name!!)
                }
            }
        })

    }

    override fun onCountSelected(communityID: Int) {
        (activity!! as MainActivity).addFragment(CommunityMemberFragment(communityID),false,
                false,R.id.frameLayout,null)
    }
}
