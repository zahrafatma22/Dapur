package com.example.dapur.dapur

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dapur.databinding.ActivityDapurBinding
import com.example.dapur.db.MappingHelper
import com.example.dapur.db.PesananHelper
import com.example.dapur.model.PesananModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class DapurActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDapurBinding
    private lateinit var pesananHelper: PesananHelper
    private lateinit var adapterr: DapurAdapter

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDapurBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Dapur"

        adapterr = DapurAdapter(object : DapurAdapter.IOnItemClickCallback {
            override fun onItemClicked(data: PesananModel, position: Int) {
                showDialog(data, position)
            }
        })
        showListDapur()
    }

    fun showListDapur() {
        lifecycleScope.launch {
            pesananHelper = PesananHelper.getInstance(applicationContext)
            pesananHelper.open()

            val defferedPesanan = async(Dispatchers.IO) {
                val cursor = pesananHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val dataDapur = defferedPesanan.await()
            if (dataDapur.size >0) {
                adapterr.listDapur = dataDapur
            } else {
                adapterr.listDapur = arrayListOf()
            }
            pesananHelper.close()
            showRvDapur()
        }
    }

    fun showRvDapur() {
        binding.idrvDapur.apply {
            adapter = adapterr
            layoutManager = LinearLayoutManager(this@DapurActivity)
        }
    }

    fun showDialog(data: PesananModel, position: Int) {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Confirmasi")
            .setMessage("Apakah menu sudah siap ?")
            .setPositiveButton("Sudah") { _, _ ->
                pesananHelper.open()
                pesananHelper.deleteById(data.id.toString())
                adapterr.delete(position)
                showRvDapur()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}