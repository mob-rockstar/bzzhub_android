package smartdev.bzzhub.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import smartdev.bzzhub.ui.activity.UserProfileActivity
import smartdev.bzzhub.ui.adapter.CommunityBubbleMemberAdapter
import smartdev.bzzhub.ui.adapter.CommunityMemberAdapter
import smartdev.bzzhub.ui.base.BaseFragment
import smartdev.bzzhub.util.Constant
import smartdev.bzzhub.util.navigation.Arg
import smartdev.bzzhub.util.navigation.NavigationManager
import java.io.File

class CommunityMemberFragment(private val mCommunityId: Int) : BaseFragment(), CommunityBubbleMemberAdapter.SelectedListener,
CommunityMemberAdapter.SelectedListener{

    var mContext: Context? = null
    var communityBubbleAdapter : CommunityBubbleMemberAdapter? = null
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

    private var selectorList = ArrayList<SelectorResponse.Result>()
    private var countries = ArrayList<CountryResponse.Result>()
    private var cities = ArrayList<CityResponse.Result>()
    private var allCities = ArrayList<CityResponse.Result>()
    private var cityAdapter: ArrayAdapter<CityResponse.Result>? = null
    private var pageId = 1
    private var communityMemberAdapter: CommunityMemberAdapter? = null
    private var isLoading = false
    private var loadMore = true
    private var communityMemberList = ArrayList<CommunityMemberResponse.Result>()
    private var interests: ArrayList<InterestResponse.Result> = ArrayList<InterestResponse.Result>()
    private var interestAdapter: ArrayAdapter<InterestResponse.Result>? = null
    private var interestID: Int = 0
    private var imageGroup : File? = null
    private var edittextFileName : EditText? = null

    private var communityId: Int = 0

    private lateinit var easyImage: EasyImage
    private var file: File? = null
    private var imageRequestCode = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_community_member, container, false)

        // Define Easy Image
        easyImage =
                EasyImage.Builder(activity!!).setCopyImagesToPublicGalleryFolder(false)
                        .allowMultiple(false)
                        .build()

        communityId = mCommunityId
        tvFindCommunity = view.findViewById(R.id.tv_find_company)
        recyclerViewTab = view.findViewById(R.id.recyclerView_tab)
        ivClose = view.findViewById(R.id.iv_close)
        recyclerView = view.findViewById(R.id.recyclerView)

        allCities = Constant.getInstance().cities
        countries = Constant.getInstance().countries
        selectorList = Constant.getInstance().selectorList

        initRecyclerViews()
        initTitle()

        this.layoutSearchBar = view.findViewById(R.id.layout_search_bar)
        this.layoutSearchBar.setOnClickListener {
       //     openSearchCommunityDialog()
        }
        //      getAllCompanies()

        ivClose.setOnClickListener {
            communityMemberList.clear()
            communityMemberAdapter!!.setItems(communityMemberList)
            pageId = 1
            getCommunityMembers()
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
        communityMemberList = ArrayList()
        if (communityMemberAdapter != null){
            communityMemberAdapter!!.setItems(communityMemberList)
        }

        getCommunityMembers()
    }

    fun initRecyclerViews(){
        recyclerViewTab!!.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false)
        recyclerView!!.layoutManager = LinearLayoutManager(mContext)

        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == communityMemberList.size - 1) {

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

        communityBubbleAdapter = CommunityBubbleMemberAdapter(context!!,titleList)
        recyclerViewTab!!.adapter = communityBubbleAdapter
        communityBubbleAdapter!!.listener = this@CommunityMemberFragment
    }

/*
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
                    layoutMembers.visibility = View.GONE
                    tvSector.visibility = View.VISIBLE
                    layoutSector.visibility = View.VISIBLE
                    tvFindCommunity.text = activity!!.resources.getString(R.string.str_search_member)
                }else if (tab!!.position == 1){
                    layoutMembers.visibility = View.VISIBLE
                    tvSector.visibility = View.GONE
                    layoutSector.visibility = View.GONE
                    tvFindCommunity.text = activity!!.resources.getString(R.string.str_search_member)
                }
            }
        })

*/
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
        }*//*

        val editTextKeyword = view.findViewById<EditText>(R.id.editttext_key_word)
        if (ivClose.visibility == View.VISIBLE) {
            editTextKeyword.setText(tvFindCommunity!!.text.toString().trim { it <= ' ' })
        }

        val adapter: ArrayAdapter<SelectorResponse.Result> = ArrayAdapter<SelectorResponse.Result>(currentActivity, android.R.layout.simple_spinner_dropdown_item, selectorList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSector.adapter = adapter
        spinnerSector.setSelection(selectorList.size - 1)

        spinnerSector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                selectorId = selectorList[position].subId
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
        spinnerInterest.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                interestID = interests[position].interestId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

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
        val tvSearchCompany = view.findViewById<TextView>(R.id.tv_search)
        tvSearchCompany.setOnClickListener {
            searchKey = editTextKeyword.text.toString().trim { it <= ' ' }
            countryId = countries[spinnerCountry.selectedItemPosition].countryId
            cityId = cities[spinnerCity.selectedItemPosition].cityId
            selectorId = selectorList[spinnerSector.selectedItemPosition].categoryId
            pageId = 1
            //   getCommunityList()
            */
/*
            mainPageAdapter.setProducts(java.util.ArrayList<Company>())
            companies = java.util.ArrayList<Company>()
            searchCompanyList()*//*

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
*/

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
        spinnerCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                cityId = cities[position].cityId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    fun getCommunityMembers(){
        BzzApp.getBzHubRepository().getCommunityMemberList(communityId,pageId)
                .subscribe(object : Observer<CommunityMemberResponse> {
                    lateinit var mDisposable: Disposable
                    override fun onComplete() {
                        mDisposable.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposable = d
                    }

                    override fun onNext(t: CommunityMemberResponse) {
                        isLoading = t.result == null || t.result.size != 10

                        communityMemberList .clear()
                        if (t.result != null) {
                            communityMemberList = t.result
                            if (loadMore) {
                                loadMore = false
                                pageId += 1
                            }
                        }

                        if (communityMemberAdapter == null){

                            communityMemberAdapter = CommunityMemberAdapter(activity!!, t.result)
                            communityMemberAdapter!!.listener = this@CommunityMemberFragment
                            recyclerView!!.adapter = communityMemberAdapter
                        }else{
                            communityMemberAdapter!!.addItems(communityMemberList)
                        }

                    }

                    override fun onError(e: Throwable) {
                        this@CommunityMemberFragment.onError(e)
                    }
                })
    }

/*    fun searchCommunities(){
        BzzApp.getBzHubRepository().searchCommunities(searchKey,selectorId!!)
                .subscribe(object : Observer<CommunityListResponse> {
                    lateinit var mDisposable: Disposable
                    override fun onComplete() {
                        mDisposable.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDisposable = d
                    }

                    override fun onNext(t: CommunityListResponse) {
                        communityAdapter = CommunityAdapter(activity!!, t.result)
                        recyclerView!!.adapter = communityAdapter
                    }

                    override fun onError(e: Throwable) {
                        this@CommunityMemberFragment.onError(e)
                    }
                })
    }*/
/*
    override fun onSelected(communityID: Int) {
        Log.d("loginFlags",Constant.getInstance().loginFlag.toString())
        if (Constant.getInstance().loginFlag != 3){
            var intent = Intent(activity!!, CommunityDetailActivity::class.java)
            intent.putExtra("community_id",communityID)
            startActivity(intent)
        }else{

        }

    }*/

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
            4->{
                activity!!.supportFragmentManager.popBackStack()
            }
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

    override fun onMemberSelected(member: CommunityMemberResponse.Result) {
        if (member.flag == 1){
            val bundle = Bundle()
            bundle.putInt(Arg.ARG_COMPANY_ID, member.userOrCompanyMembersId)
            bundle.putInt(Arg.ARG_CONNECTED_STATUS, 1)
            NavigationManager.gotoCompanyDetailsActivity(currentActivity!!,bundle)
        }else{
            val intent = Intent(currentActivity,UserProfileActivity::class.java)
            intent.putExtra("User_id",member.userOrCompanyMembersId)
            startActivity(intent)
        }
    }
}