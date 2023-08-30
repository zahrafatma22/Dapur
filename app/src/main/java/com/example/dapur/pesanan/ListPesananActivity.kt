package com.example.dapur.pesanan

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dapur.DashboardActivity
import com.example.dapur.databinding.ActivityListPesananBinding
import com.example.dapur.db.MappingHelper
import com.example.dapur.db.PesananHelper
import com.example.dapur.key.Key
import com.example.dapur.menu.KategoriActivity
import com.example.dapur.model.PesananModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ListPesananActivity: AppCompatActivity() {
    private lateinit var binding: ActivityListPesananBinding
    private lateinit var adapterr: ListPesananAdapter
    private lateinit var pesananHelper: PesananHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val getNoMeja = intent.getStringExtra(Key.KEY_NO_MEJA)!!

        binding.idbtnKirim.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        binding.idbtnTambah.setOnClickListener {
            val intent = Intent(this, KategoriActivity::class.java)
            intent.putExtra(Key.KEY_NO_MEJA, getNoMeja)
            startActivity(intent)
        }

        adapterr = ListPesananAdapter(object : ListPesananAdapter.IOnItemClickCallback {
            override fun onItemClicked(data: PesananModel, position: Int) {
                val dialog = AlertDialog.Builder(this@ListPesananActivity)
                dialog.setTitle("Hapus")
                    .setMessage("Apakah anda yakin menghapus menu ini ?")
                    .setPositiveButton("YA") { _, _ ->
                        pesananHelper.open()
                        pesananHelper.deleteById(data.id.toString())
                        adapterr.delete(position)
                        showListPesanan()
                    }
                    .setNegativeButton("BATAL") { dialog, _ ->
                        dialog.cancel()
                    }
                    .create()
                    .show()
            }
        })
        showPesanan(getNoMeja)
    }

    fun showPesanan(noMeja: String) {
        lifecycleScope.launch {
            pesananHelper = PesananHelper.getInstance(applicationContext)
            pesananHelper.open()

            val defferedPesanan = async(Dispatchers.IO) {
                val cursor = pesananHelper.queryByNoMeja(noMeja)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val pesanan = defferedPesanan.await()
            if (pesanan.size > 0) {
                adapterr.listPesanan = pesanan
            } else {
                adapterr.listPesanan = arrayListOf()
            }
            pesananHelper.close()

            showListPesanan()
            supportActionBar?.title = "Pesanan No Meja $noMeja"
        }
    }

    fun showListPesanan() {
        binding.idrvListpesanan.apply {
            adapter = adapterr
            layoutManager = LinearLayoutManager(this@ListPesananActivity)
        }
        var total = 0
        for (item in adapterr.listPesanan) {
            total += item.harga.toInt()
        }
        binding.idtvTotalharga.text = "Total harga $total"
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}