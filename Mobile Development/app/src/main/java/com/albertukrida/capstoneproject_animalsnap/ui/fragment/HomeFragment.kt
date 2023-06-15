package com.albertukrida.capstoneproject_animalsnap.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.data.remote.retrofit.ApiCall
import com.albertukrida.capstoneproject_animalsnap.databinding.FragmentHomeBinding

class
HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mContext = view.context
        mView = view

        ApiCall(mContext).refreshToken()

        optimizeSearchBar()
        getListHabitat()
        getListAnimal()
    }

    private fun getListHabitat(keyword: String? = null){
        val recyclerView = binding.rvListHabitat
        recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        ApiCall(mContext).getAllHabitats(mContext, binding, recyclerView, keyword)
    }

    private fun getListAnimal(keyword: String? = null){
        val recyclerView = binding.rvListAnimal
        recyclerView.layoutManager = GridLayoutManager(mContext, 2)
        ApiCall(mContext).getAllAnimal(requireActivity(), mContext, binding, recyclerView, keyword)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun optimizeSearchBar(){
        val editText = binding.edSearchBar
        editText.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                removeCursorAndKeyboard(binding.edSearchBar)
                getListHabitat(binding.edSearchBar.text.toString())
                getListAnimal(binding.edSearchBar.text.toString())
                return@OnEditorActionListener true
            }
            false
        })

        val parentLayout = binding.layoutParent
        parentLayout.setOnTouchListener { _, event ->
            if (event.action == ACTION_DOWN) {
                removeCursorAndKeyboard(binding.edSearchBar)
            }
            false
        }

        keepSearchIcon()
    }

    private fun removeCursorAndKeyboard(editText: EditText){
        editText.clearFocus()
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mView.windowToken, 0)
    }

    private fun keepSearchIcon(){
        // Get the drawable from resources
        val drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_baseline_search_24)
        // Edit Text
        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable) { }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Set the drawable as the left compound drawable
                binding.edSearchBar.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            }
        }
        binding.edSearchBar.addTextChangedListener(watcher)
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
    }
}