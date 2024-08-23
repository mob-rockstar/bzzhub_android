package smartdev.bzzhub.ui.activity

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_community_detail.*
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import pl.aprilapps.easyphotopicker.MediaFile
import pl.aprilapps.easyphotopicker.MediaSource
import smartdev.bzzhub.BzzApp
import smartdev.bzzhub.GPSTracker
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.chatmodel.Users
import smartdev.bzzhub.repository.model.*
import smartdev.bzzhub.ui.adapter.CommunityEventAdapter
import smartdev.bzzhub.ui.base.BaseActivity
import smartdev.bzzhub.util.Constant
import smartdev.bzzhub.util.FBStoreKey
import smartdev.bzzhub.util.FBStoreKey.*
import smartdev.bzzhub.util.navigation.NavigationManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CommunityDetailActivity : BaseActivity(),CommunityEventAdapter.SelectedListener {


    var communityID: Int = 0
    var communityDetail: CommunityDetailResponse.Result? = null

    private var spinnerCity: Spinner? = null
    private  var spinnerCountry:Spinner? = null
    private var spinnerBusinessCategory: Spinner? = null
    private  var spinnerSubcategory:Spinner? = null
    private var countries = ArrayList<CountryResponse.Result>()
    private var cities = ArrayList<CityResponse.Result>()
    private var allCities = ArrayList<CityResponse.Result>()
    private var cityAdapter: ArrayAdapter<CityResponse.Result>? = null
    var subcategories = ArrayList<SubCategoryResponse.Result>()
    private var countryId = 0
    private  var cityId:Int? = 0
    private var imageGroup : File? = null
    private var edittextFileName : EditText ? = null
    private var selectorId: Int? = null
    private var selectorList = ArrayList<SelectorResponse.Result>()
    private var description: String = ""
    private var title: String = ""
    var subcategoryId :Int = 0
    private var communityEventAdapter:  CommunityEventAdapter? = null
    private val mCalendar = Calendar.getInstance()
    private lateinit var  tvDate :TextView
    private lateinit var  tvTime :TextView

    private  var imageEvent: File ?= null
    private var editTextEventFileName : EditText? = null
    var memberIDs: ArrayList<Int> = ArrayList()
    var members: ArrayList<Users> = ArrayList()
    private lateinit var easyImage: EasyImage
    private var file: File? = null
    private var imageRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community_detail)
        communityID =intent.getIntExtra("community_id",0)
        recyclerView.layoutManager = GridLayoutManager(this,2)

        // Define Easy Image
        easyImage =
                EasyImage.Builder(this@CommunityDetailActivity).setCopyImagesToPublicGalleryFolder(false)
                        .allowMultiple(false)
                        .build()

        currentActivity = this
        iv_back.setOnClickListener { finish() }

        selectorList = (Constant.getInstance().selectorList)
        countries = Constant.getInstance().countries
        allCities = Constant.getInstance().cities

        tv_add_event.setOnClickListener {
            dialogAddEventDialog()
        }
        getCommunityDetails()
        getCommunityActivities()
        layout_edit.setOnClickListener{
            openEditCommunityDialog()
        }

        layout_login.setOnClickListener {
            joinGroup()
        }

        layout_logout.setOnClickListener {
            leaveGroup()
        }

        layout_delete.setOnClickListener {
            showDeleteGroupDialog()
        }

        layout_chat.setOnClickListener {
            val intent = Intent(this@CommunityDetailActivity,ChatActivity::class.java)
            intent.putExtra("community_id",communityID)
            intent.putExtra("community_name",communityDetail?.title)

            startActivity(intent)
        }
    }

    private fun getCommunityDetails(){
        BzzApp.getBzHubRepository().getCommunityDetail(communityID)
                .subscribe(
                        object : Observer<CommunityDetailResponse> {
                            lateinit var mDisposable: Disposable
                            override fun onComplete() {
                                mDisposable.dispose()
                            }

                            override fun onSubscribe(d: Disposable) {
                                mDisposable = d
                            }

                            override fun onNext(t: CommunityDetailResponse) {
                                if (t.code == 200 && t.status){
                                    communityDetail = t.result
                                    initUI()
                                }
                            }

                            override fun onError(e: Throwable) {
                                this@CommunityDetailActivity.onError(e)
                            }
                        }
                )
    }

    fun initUI(){
        Glide.with(this).load(communityDetail!!.image).centerCrop().into(iv_banner)
        tv_title.text  = communityDetail!!.title
        tv_member_count.text = communityDetail!!.members.toString()
        tvDescription.text = communityDetail!!.description

        if (Constant.getInstance().loginFlag == 3){
            tv_add_event.visibility = GONE
            layout_edit.visibility = GONE
            layout_delete.visibility = GONE
            layout_login.visibility = GONE
            layout_logout.visibility = GONE
            layout_chat.visibility = GONE
        }else{
            initFunctionBar()
        }
    }

    fun initFunctionBar(){
        if (communityDetail!!.userOrCompanyId == Constant.getInstance().loginID
                && communityDetail!!.flag == Constant.getInstance().loginFlag){
            layout_edit.visibility = VISIBLE
            layout_delete.visibility = VISIBLE
            layout_login.visibility = GONE
            layout_logout.visibility = GONE
            layout_chat.visibility = VISIBLE
            tv_add_event.visibility = VISIBLE
        }else{
            tv_add_event.visibility = GONE
            layout_edit.visibility = GONE
            layout_delete.visibility = GONE
            if (communityDetail!!.isMember == 1){
                layout_login.visibility = GONE
                layout_logout.visibility = VISIBLE
                layout_chat.visibility = VISIBLE
            }else{
                layout_login.visibility = VISIBLE
                layout_logout.visibility = GONE
                layout_chat.visibility = GONE
            }
        }

    }

    private fun openEditCommunityDialog() {
        val dialog = Dialog(currentActivity)
        val layoutInflater = LayoutInflater.from(currentActivity)

        val view = layoutInflater.inflate(R.layout.dialog_edit_group, null)
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
        spinnerCountry.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                countryId = countries[position].countryId
                if (position != countries.size - 1) {
                    filterCities(countries[position].countryId)
                    setCityAdapter()
                } else {
                    cities = ArrayList<CityResponse.Result>()
                    setCityAdapter()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        for (i in countries.indices){
            if (countries[i].countryId == communityDetail!!.countryId!!){
                spinnerCountry.setSelection(i)
            }
        }

        spinnerSubcategory = dialog.findViewById(R.id.spinner_category)
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

        if (selectorList.isNotEmpty()){
            for ( i in selectorList.indices){
                if (selectorList[i].categoryId == communityDetail?.categoryId){
                    spinnerSector.setSelection(i)
                }
            }
        }

        (view.findViewById<RelativeLayout>(R.id.layout_select_file)) .setOnClickListener {
            imageRequestCode = 100

            RxPermissions(this@CommunityDetailActivity).request(
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

        edittextFileName = view.findViewById(R.id.edittext_file_name)

        edittextFileName!!.setOnClickListener {
            imageRequestCode = 100

            RxPermissions(this@CommunityDetailActivity).request(
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
        val edittextTitle = view.findViewById<EditText>(R.id.edittext_title)
        val edittextDescription = view.findViewById<EditText>(R.id.edittext_description)

        edittextTitle.setText(communityDetail?.title!!)
        edittextDescription.setText(communityDetail?.description!!)
        var tvAdd = view.findViewById<TextView>(R.id.tv_add)
        tvAdd.setOnClickListener {
            title = edittextTitle.text.toString().trim()
            description = edittextDescription.text.toString().trim()
            if (cityId == null || subcategoryId ==  null || subcategoryId == 0){
                Toast.makeText(currentActivity, resources.getString(R.string.str_make_sure_valid_field), Toast.LENGTH_LONG).show()
            }else{
                dialog.dismiss()
                editCommunity(title,selectorId!!,description,cityId!!)
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
                            subcategories = subCategoryResponse.result as ArrayList<SubCategoryResponse.Result>
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
                            if (communityDetail!!.categoryId != null) {
                                for (i in subcategories.indices) {
                                    if (subcategories[i].subId === communityDetail!!.sectorId) {
                                        spinnerSubcategory!!.setSelection(i)
                                    }
                                }
                            } else {
                                spinnerSubcategory!!.setSelection(subcategories.size - 1)
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

    private fun filterCities(countryId: Int) {
        cities = java.util.ArrayList()
        if (allCities != null && allCities.isNotEmpty()) {
            for (city in allCities) {
                if (city.countryId == countryId) {
                    cities.add(city)
                }
            }
        }
    }

    private fun setEventCityAdapter(eventCityID: Int) {
        val result = CityResponse.Result(0, 0, "- Select -")
        cities.add(result)
        cityAdapter = ArrayAdapter(currentActivity, android.R.layout.simple_spinner_dropdown_item, cities)
        cityAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity!!.adapter = cityAdapter
        spinnerCity!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                cityId = cities[position].cityId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        for (i in cities.indices){
            if (cities[i].cityId == eventCityID){
                spinnerCity!!.setSelection(i)
            }
        }
    }


    private fun setCityAdapter() {
        val result = CityResponse.Result(0, 0, "- Select -")
        cities.add(result)
        cityAdapter = ArrayAdapter(currentActivity, android.R.layout.simple_spinner_dropdown_item, cities)
        cityAdapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity!!.adapter = cityAdapter
        spinnerCity!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                cityId = cities[position].cityId
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        for (i in cities.indices){
            if (cities[i].cityId == communityDetail!!.cityId){
                spinnerCity!!.setSelection(i)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Profile Image Changed
        easyImage.handleActivityResult(requestCode, resultCode, data, this@CommunityDetailActivity, object :
                DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {

                if (imageRequestCode == 100) {
                    try {
                        imageGroup = imageFiles.first().file
                        edittextFileName!!.setText(imageGroup!!.name!!)
                        //  Glide.with(currentActivity).load(imageGroup!!.absolutePath).centerCrop().into(ivDialogProfile)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }else if (imageRequestCode == 102){
                    imageEvent = imageFiles.first().file
                    editTextEventFileName!!.setText(imageEvent!!.name!!)
                }
            }
        })

    }

    fun joinGroup(){
        BzzApp.getBzHubRepository().joinCommunity(communityID,Constant.getInstance().loginID,Constant.getInstance().loginFlag)
                .subscribe(object : Observer<SimpleResponse> {
                     lateinit var disposable: Disposable
                    override fun onComplete() {
                        disposable.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: SimpleResponse) {
                        (currentActivity as CommunityDetailActivity).showSnackBar(t.message,true)
                        communityDetail!!.isMember = 1
                        initFunctionBar()
                        BzzApp.getFirestore().collection(KEY_GROUP).document(communityID.toString()).collection(KEY_USERS)
                                .get().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        memberIDs = ArrayList()
                                        members = ArrayList()
                                        var isAlreadyMember = false
                                        for (document in task.result!!) {
                                            members.add(Users(document))
                                            if (Users(document).userID == Constant.getInstance().loginID && Users(document).userFlag == Constant.getInstance().loginFlag){
                                                isAlreadyMember = true
                                            }
                                        }
                                        if (!isAlreadyMember){
                                            val batch = BzzApp.getFirestore().batch()
                                            var taskMap: HashMap<String, Any> = HashMap()
                                            taskMap.put(FBStoreKey.KEY_FLAG, Constant.getInstance().loginFlag)
                                            taskMap.put(FBStoreKey.KEY_USER_ID, Constant.getInstance().loginID)
                                            taskMap.put(KEY_USER_NAME,Constant.getInstance().loginName)
                                            taskMap.put(KEY_USER_PROFILE_IMAGE,Constant.getInstance().loginImage)
                                            batch[BzzApp.getFirestore().collection(KEY_GROUP).document(communityID.toString()).collection(KEY_USERS)
                                                    .document()] = taskMap
                                            batch.commit().addOnCompleteListener { task: Task<Void?> ->
                                                if (task.isSuccessful) {

                                                } else {
                                                    Toast.makeText(applicationContext, resources.getString(R.string.str_error), Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                        }

                                    }
                                }
                    }

                    override fun onError(e: Throwable) {
                        (currentActivity as CommunityDetailActivity).onError(e)
                    }
                })
    }

    fun leaveGroup(){
        BzzApp.getBzHubRepository().leaveCommunity(communityID,Constant.getInstance().loginID,Constant.getInstance().loginFlag)
                .subscribe(object : Observer<SimpleResponse> {
                    lateinit var disposable: Disposable
                    override fun onComplete() {
                        disposable.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d

                    }

                    override fun onNext(t: SimpleResponse) {
                        (currentActivity as CommunityDetailActivity).showSnackBar(t.message, true)
                        communityDetail!!.isMember = 0
                        initFunctionBar()

                        BzzApp.getFirestore().collection(KEY_GROUP).document(communityID.toString()).collection(KEY_USERS)
                                .get().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        memberIDs = ArrayList()
                                        members = ArrayList()
                                        var isAlreadyMember = false
                                        var uID = ""
                                        for (document in task.result!!) {
                                            members.add(Users(document))
                                            if (Users(document).userID == Constant.getInstance().loginID && Users(document).userFlag == Constant.getInstance().loginFlag) {
                                                isAlreadyMember = true
                                                uID = Users(document).uid
                                            }
                                        }
                                        if (isAlreadyMember){
                                            BzzApp.getFirestore().collection(KEY_GROUP).document(communityID.toString()).collection(KEY_USERS)
                                                    .document(uID).delete().addOnSuccessListener {

                                                    }
                                        }

                                    }
                                }

                    }

                    override fun onError(e: Throwable) {
                        (currentActivity as CommunityDetailActivity).onError(e)
                    }
                })

    }


    fun deleteGroup(){
        BzzApp.getBzHubRepository().deleteCommunity(communityID)
                .subscribe(object : Observer<SimpleResponse> {
                    lateinit var disposable: Disposable
                    override fun onComplete() {
                        disposable.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        disposable = d
                    }

                    override fun onNext(t: SimpleResponse) {
                        finish()
                    }

                    override fun onError(e: Throwable) {
                        (currentActivity as CommunityDetailActivity).onError(e)
                    }
                })
    }

    private fun showDeleteGroupDialog() {
        val dialog = Dialog(currentActivity)
        val layoutInflater = LayoutInflater.from(currentActivity)
        val view = layoutInflater.inflate(R.layout.dialog_delete_group, null)
        dialog.window
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        Objects.requireNonNull(dialog.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val tvYes = dialog.findViewById<TextView>(R.id.tv_yes)
        val tvCancel = dialog.findViewById<TextView>(R.id.tv_cancel)
        tvYes.setOnClickListener {
            dialog.dismiss()
            deleteGroup()
        }
        tvCancel.setOnClickListener { dialog.dismiss() }
        val layoutRoot = dialog.findViewById<LinearLayout>(R.id.layout_root)
        layoutRoot.setOnClickListener { dialog.dismiss() }
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

    private fun dialogAddEventDialog() {
        val dialog = Dialog(currentActivity)
        val layoutInflater = LayoutInflater.from(currentActivity)
        val view = layoutInflater.inflate(R.layout.dialog_add_event, null)
        dialog.window
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        Objects.requireNonNull(dialog.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val edittextTitle = view.findViewById<EditText>(R.id.edittext_title)
        val layoutDate = view.findViewById<RelativeLayout>(R.id.layout_date)
        val layoutTime = view.findViewById<RelativeLayout>(R.id.layout_time)
        val editTextDescription  =view.findViewById<EditText>(R.id.edittext_description)
        val layoutSelectFile = view.findViewById<RelativeLayout>(R.id.layout_select_file)
        tvDate = view.findViewById<TextView>(R.id.tv_date)
        tvTime = view.findViewById<TextView>(R.id.tv_time)

        val spinnerCountry = view.findViewById<Spinner>(R.id.spinner_country)
        val countrySpinnerAdapter: ArrayAdapter<CountryResponse.Result> = ArrayAdapter<CountryResponse.Result>(currentActivity, android.R.layout.simple_spinner_dropdown_item, countries)
        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = countrySpinnerAdapter
        spinnerCountry.setSelection(countries.size - 1)

        editTextEventFileName = view.findViewById(R.id.edittext_file_name)
        spinnerCity = view.findViewById<Spinner>(R.id.spinner_city)
        spinnerCountry.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                countryId = countries[position].countryId
                if (position != countries.size - 1) {
                    filterCities(countries[position].countryId)
                    setCityAdapter()
                } else {
                    cities = ArrayList<CityResponse.Result>()
                    setCityAdapter()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        layoutSelectFile.setOnClickListener{
            imageRequestCode = 102

            val permissions = RxPermissions(this@CommunityDetailActivity)
            permissions.request(    Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe { granted: Boolean ->
                        if (granted) {
                            easyImage.openChooser(this)
                        }
                    }

        }

        editTextEventFileName!!.setOnClickListener {
            imageRequestCode = 102

            RxPermissions(this@CommunityDetailActivity).request(
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

        layoutDate.setOnClickListener {
            // TODO Auto-generated method stub
            val datePickerDialog = DatePickerDialog(currentActivity, R.style.AppCompatDatePickerDialogStyle, dateStartDate, mCalendar
                    .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }

        layoutTime.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog

            mTimePicker = TimePickerDialog(this@CommunityDetailActivity, R.style.AppCompatDatePickerDialogStyle, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute -> tvTime.setText("$selectedHour:$selectedMinute") }, hour, minute, true) //Yes 24 hour time

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()

        }

        val tvDone = view.findViewById<TextView>(R.id.tv_done)
        tvDone.setOnClickListener {
            val strTitle = edittextTitle.text.toString().trim()
            val strDescription = editTextDescription.text.toString().trim()
            val strDate = tvDate.text.toString().trim()
            val strTime = tvTime.text.toString().trim()
            val strEventDate = "$strDate $strTime"
            if (strTitle.isNotEmpty() && strDescription.isNotEmpty() && strDate.isNotEmpty() && strTime.isNotEmpty()
                    && editTextEventFileName!!.text.toString().trim().isNotEmpty()){
                createEvent(strEventDate,strTitle,strDescription)
                dialog.dismiss()
            }else{
                Toast.makeText(currentActivity, resources.getString(R.string.str_make_sure_valid_field), Toast.LENGTH_LONG).show()
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

    var dateStartDate = OnDateSetListener { _: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
        // TODO Auto-generated method stub
        mCalendar.set(Calendar.YEAR, year)
        mCalendar.set(Calendar.MONTH, monthOfYear)
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        updateStartDate()
    }


    private fun updateStartDate() {
        val myFormat = "dd-MM-yy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        tvDate!!.text = sdf.format(mCalendar.time)
    }


    fun editCommunity(title: String,sectorID: Int,description:String,cityID:Int){
        if (title.isNotEmpty() && description.isNotEmpty()){
            BzzApp.getBzHubRepository().editCommunity(communityID,subcategoryId,Constant.getInstance().loginID,title,description,
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
                                getCommunityDetails()
                            }else{
                                this@CommunityDetailActivity.onError(Exception(t.message))
                            }
                        }

                        override fun onError(e: Throwable) {
                            this@CommunityDetailActivity.onError(e)
                        }
                    }
            )
        }else{
            Toast.makeText(currentActivity, resources.getString(R.string.str_make_sure_valid_field), Toast.LENGTH_LONG).show()
        }
    }

    private fun getCommunityActivities(){
        BzzApp.getBzHubRepository().getCommunityEvents(communityID,1).subscribe(
                object : Observer<CommunityEventResponse> {
                    lateinit var  mDispose:Disposable
                    override fun onComplete() {
                        mDispose.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(t: CommunityEventResponse) {
                        if (t.code == 200 && t.status && t.result != null){
                            communityEventAdapter = CommunityEventAdapter(this@CommunityDetailActivity,t.result)
                            recyclerView.adapter = communityEventAdapter
                            communityEventAdapter?.listener = this@CommunityDetailActivity
                        }
                    }

                    override fun onError(e: Throwable) {

                    }
                }
        )
    }

    fun createEvent(eventDate: String,title: String,description: String){
        BzzApp.getBzHubRepository().createEvent(communityID,eventDate,Constant.getInstance().loginID,title,description,Constant.getInstance().loginFlag,
        cityId!!,imageEvent!!)
                .subscribe(object : Observer<SimpleResponse> {
                    lateinit var  mDispose:Disposable
                    override fun onComplete() {
                        mDispose.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(t: SimpleResponse) {
                        if (t.code == 200 && t.status ){
                            getCommunityActivities()
                        }
                        showSnackBar(t.message,t.code == 200)
                    }

                    override fun onError(e: Throwable) {

                    }
                })

    }


    private fun dialogEditEventDialog(event: CommunityEventResponse.Result) {
        val dialog = Dialog(currentActivity)
        val layoutInflater = LayoutInflater.from(currentActivity)
        val view = layoutInflater.inflate(R.layout.dialog_edit_event, null)
        dialog.window
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(view)
        Objects.requireNonNull(dialog.window)!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val edittextTitle = view.findViewById<EditText>(R.id.edittext_title)
        val layoutDate = view.findViewById<RelativeLayout>(R.id.layout_date)
        val layoutTime = view.findViewById<RelativeLayout>(R.id.layout_time)
        val editTextDescription  =view.findViewById<EditText>(R.id.edittext_description)
        val layoutSelectFile = view.findViewById<RelativeLayout>(R.id.layout_select_file)
        tvDate = view.findViewById<TextView>(R.id.tv_date)
        tvTime = view.findViewById<TextView>(R.id.tv_time)

        val dateTime = event.eventDate
        val splited = dateTime!!.split(" ")

        edittextTitle.setText( event.title!!)
        editTextDescription.setText(event.description)

        if (splited != null && splited.isNotEmpty() && splited.size == 2){
            tvDate.text = splited.first()
            tvTime.text = splited[1]
        }

        val spinnerCountry = view.findViewById<Spinner>(R.id.spinner_country)
        val countrySpinnerAdapter: ArrayAdapter<CountryResponse.Result> = ArrayAdapter<CountryResponse.Result>(currentActivity, android.R.layout.simple_spinner_dropdown_item, countries)
        countrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCountry.adapter = countrySpinnerAdapter
        spinnerCountry.setSelection(countries.size - 1)


        Log.d("cityIDIS",event!!.cityId.toString())
        for (i in countries.indices){
            if (countries[i].countryId == event!!.countryId!!){
                spinnerCountry.setSelection(i)

            }
        }

        editTextEventFileName = view.findViewById(R.id.edittext_file_name)
        spinnerCity = view.findViewById<Spinner>(R.id.spinner_city)
        spinnerCountry.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                countryId = countries[position].countryId
                if (position != countries.size - 1) {
                    filterCities(countries[position].countryId)
                    setEventCityAdapter(event.cityId)
                } else {
                    cities = ArrayList<CityResponse.Result>()
                    setEventCityAdapter(event.cityId)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        layoutSelectFile.setOnClickListener{
            imageRequestCode = 102

            RxPermissions(this@CommunityDetailActivity).request(
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

        editTextEventFileName!!.setOnClickListener {
            imageRequestCode = 102

            RxPermissions(this@CommunityDetailActivity).request(
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

        layoutDate.setOnClickListener {
            // TODO Auto-generated method stub
            val datePickerDialog = DatePickerDialog(currentActivity, R.style.AppCompatDatePickerDialogStyle, dateStartDate, mCalendar
                    .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                    mCalendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            datePickerDialog.show()
        }

        layoutTime.setOnClickListener {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
            val minute = mcurrentTime[Calendar.MINUTE]
            val mTimePicker: TimePickerDialog

            mTimePicker = TimePickerDialog(this@CommunityDetailActivity, R.style.AppCompatDatePickerDialogStyle, TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute -> tvTime.setText("$selectedHour:$selectedMinute") }, hour, minute, true) //Yes 24 hour time

            mTimePicker.setTitle("Select Time")
            mTimePicker.show()

        }

        val tvDone = view.findViewById<TextView>(R.id.tv_update)
        tvDone.setOnClickListener {
            val strTitle = edittextTitle.text.toString().trim()
            val strDescription = editTextDescription.text.toString().trim()
            val strDate = tvDate.text.toString().trim()
            val strTime = tvTime.text.toString().trim()
            val strEventDate = "$strDate $strTime"
            if (strTitle.isNotEmpty() && strDescription.isNotEmpty() && strDate.isNotEmpty() && strTime.isNotEmpty()){
                updateEvent(event.eventId,strEventDate,strTitle,strDescription)
                dialog.dismiss()
            }else{
                Toast.makeText(currentActivity, resources.getString(R.string.str_make_sure_valid_field), Toast.LENGTH_LONG).show()
            }
        }

        val tvDelete = view.findViewById<TextView>(R.id.tv_delete)

        tvDelete.setOnClickListener {
            deleteEvent(event.eventId)
            dialog.dismiss()
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

    fun updateEvent(eventId : Int,eventDate: String,title: String,description: String){
        BzzApp.getBzHubRepository().updateEvent(eventId,eventDate,title,description,cityId!!,imageEvent)
                .subscribe(object : Observer<SimpleResponse> {
                    lateinit var  mDispose:Disposable
                    override fun onComplete() {
                        mDispose.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(t: SimpleResponse) {
                        if (t.code == 200 && t.status ){
                            getCommunityActivities()
                        }
                        showSnackBar(t.message,t.code == 200)
                    }

                    override fun onError(e: Throwable) {

                    }
                })
    }

    fun deleteEvent(eventId : Int){
        BzzApp.getBzHubRepository().deleteEvent(eventId)
                .subscribe(object : Observer<SimpleResponse> {
                    lateinit var  mDispose:Disposable
                    override fun onComplete() {
                        mDispose.dispose()
                    }

                    override fun onSubscribe(d: Disposable) {
                        mDispose = d
                    }

                    override fun onNext(t: SimpleResponse) {
                        if (t.code == 200 && t.status ){
                            getCommunityActivities()
                        }
                        showSnackBar(t.message,t.code == 200)
                    }

                    override fun onError(e: Throwable) {

                    }
                })
    }

    override fun onSelected(event: CommunityEventResponse.Result) {
        if (Constant.getInstance().loginFlag != 3 && communityDetail!!.userOrCompanyId == Constant.getInstance().loginID
                && communityDetail!!.flag == Constant.getInstance().loginFlag){
            dialogEditEventDialog(event)
        }
    }
}
