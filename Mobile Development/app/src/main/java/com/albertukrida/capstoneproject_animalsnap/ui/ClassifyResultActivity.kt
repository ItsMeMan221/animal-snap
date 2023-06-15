package com.albertukrida.capstoneproject_animalsnap.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import com.albertukrida.capstoneproject_animalsnap.R
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.DonasiItem
import com.albertukrida.capstoneproject_animalsnap.data.remote.response.HabitatItem
import com.albertukrida.capstoneproject_animalsnap.databinding.ActivityClassifyResultBinding
import com.albertukrida.capstoneproject_animalsnap.helper.IntentHelper
import com.albertukrida.capstoneproject_animalsnap.helper.Utils
import com.squareup.picasso.Picasso

class ClassifyResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityClassifyResultBinding
    private lateinit var habName: String
    private lateinit var habPict: String
    private lateinit var habDesc: String

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        supportActionBar?.title = resources.getString(R.string.classify)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassifyResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(HABITATS.size == 1){
            habName = HABITATS[0].namaHabitat
            habPict = HABITATS[0].gambarHabitat
            habDesc = HABITATS[0].deskripsiHabitat
        }else{
            for ((i, habitat) in HABITATS.withIndex()){
                habName = if(i == 0){
                    habitat.namaHabitat
                }else{
                    "$habName, ${habitat.namaHabitat}"
                }
            }
        }

        binding.tvName.text = NAME
        binding.tvClass.text = CLASS
        binding.tvStatus.text = STATUS
        binding.tvHabitats.text = habName
        binding.tvDesc.text = DESC
        Picasso.get().load(PICT_USER).into(binding.ivPictureUser)
        Picasso.get().load(PICT).into(binding.ivPicture)

        binding.ivPictureUser.setOnClickListener{ Utils(this).showPicture(PICT_USER) }
        binding.ivPicture.setOnClickListener{ Utils(this).showPicture(PICT) }
        binding.tvClass.setOnClickListener{ showClassDesc() }
        binding.tvHabitats.setOnClickListener{ showHabitats() }
        binding.btnDonate.setOnClickListener{ showDonate() }

        onBackPressedDispatcher.addCallback(this) {
            IntentHelper().goToHomePage(this@ClassifyResultActivity, "camera")
        }
    }

    private fun showClassDesc(){
        val showDialog = AlertDialog.Builder(this)
            .setView(R.layout.dialog_class_desc)
            .setTitle(resources.getString(R.string.class_dialog))
            .create()
        showDialog.show()

        val picture = showDialog.findViewById<ImageView>(R.id.iv_picture)!!
        val aClass = showDialog.findViewById<TextView>(R.id.tv_class)!!
        val desc = showDialog.findViewById<TextView>(R.id.tv_desc)!!

        Picasso.get().load(PICT).into(picture)
        aClass.text = CLASS
        desc.text = CLASS_DESC
    }

    private fun showHabitats(){
        val showDialog = AlertDialog.Builder(this)
            .setView(R.layout.dialog_habitats)
            .setTitle(resources.getString(R.string.habitat_dialog))
            .create()
        showDialog.show()

        val picture = showDialog.findViewById<ImageView>(R.id.iv_habitat)!!
        val habitat = showDialog.findViewById<TextView>(R.id.tv_name)!!
        val desc = showDialog.findViewById<TextView>(R.id.tv_desc)!!
        val left = showDialog.findViewById<RelativeLayout>(R.id.iv_left)!!
        val right = showDialog.findViewById<RelativeLayout>(R.id.iv_right)!!
        val type = "hab"

        Picasso.get().load(HABITATS[0].gambarHabitat).into(picture)
        habitat.text = HABITATS[0].namaHabitat
        desc.text = HABITATS[0].deskripsiHabitat
        if(HABITATS.size == 1){
            left.visibility = View.GONE
            right.visibility = View.GONE
        }else{
            var i = 0
            setVisibility(i, right, left, type)
            left.setOnClickListener{
                i -= 1
                setVisibility(i, right, left, type)
                Picasso.get().load(HABITATS[i].gambarHabitat).into(picture)
                habitat.text = HABITATS[i].namaHabitat
                desc.text = HABITATS[i].deskripsiHabitat
            }
            right.setOnClickListener{
                i += 1
                setVisibility(i, right, left, type)
                Picasso.get().load(HABITATS[i].gambarHabitat).into(picture)
                habitat.text = HABITATS[i].namaHabitat
                desc.text = HABITATS[i].deskripsiHabitat
            }
        }
    }

    private fun showDonate(){
        if(DONATION.isEmpty()){
            Toast.makeText(this, resources.getString(R.string.donate_error), Toast.LENGTH_LONG).show()
        }else{
            val showDialog = AlertDialog.Builder(this)
                .setView(R.layout.dialog_donation)
                .create()
            showDialog.show()

            val donate = showDialog.findViewById<TextView>(R.id.tv_donate)!!
            val organization = showDialog.findViewById<Button>(R.id.btn_organization)!!
            val left = showDialog.findViewById<RelativeLayout>(R.id.iv_left)!!
            val right = showDialog.findViewById<RelativeLayout>(R.id.iv_right)!!
            val type = "don"

            organization.text = DONATION[0].namaOrganisasi
            organization.setOnClickListener{ startActivity(setUrl(0)) }

            if(DONATION.size == 1){
                donate.text = resources.getString(R.string.donate_organization)
                left.visibility = View.GONE
                right.visibility = View.GONE
            }else{
                donate.text = resources.getString(R.string.donate_organization)
                var i = 0
                setVisibility(i, right, left, type)
                left.setOnClickListener{
                    i -= 1
                    setVisibility(i, right, left, type)
                    organization.text = DONATION[i].namaOrganisasi
                    organization.setOnClickListener{ startActivity(setUrl(i)) }
                }
                right.setOnClickListener{
                    i += 1
                    setVisibility(i, right, left, type)
                    organization.text = DONATION[i].namaOrganisasi
                    organization.setOnClickListener{ startActivity(setUrl(i)) }
                }
            }
        }
    }

    private fun setUrl(i: Int): Intent {
        var url = DONATION[i].linkDonasi
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://$url"
        }
        return Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }

    private fun setVisibility(i: Int, right: RelativeLayout, left: RelativeLayout, type: String){
        if((type == "hab" && i == HABITATS.size-1) || (type == "don" && i == DONATION.size-1)){
            right.visibility = View.GONE
        }else{
            right.visibility = View.VISIBLE
        }
        if(i == 0){
            left.visibility = View.GONE
        }else{
            left.visibility = View.VISIBLE
        }
    }

    companion object{
        lateinit var NAME: String
        lateinit var CLASS: String
        lateinit var STATUS: String
        lateinit var PICT: String
        lateinit var PICT_USER: String
        lateinit var DESC: String
        lateinit var CLASS_DESC: String
        lateinit var DONATION: List<DonasiItem>
        lateinit var HABITATS: List<HabitatItem>
    }
}