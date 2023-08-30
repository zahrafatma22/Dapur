package com.example.dapur.menu

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.dapur.R
import com.example.dapur.databinding.ActivityKategoriBinding
import com.example.dapur.datamenu.DataMenu
import com.example.dapur.key.Key
import com.example.dapur.model.Kategori

class KategoriActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityKategoriBinding
    private var getNoMejaPesanan: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Menu"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getNoMejaPesanan = intent.getStringExtra(Key.KEY_NO_MEJA)

        binding.idbtnMakanan.setOnClickListener(this)
        binding.idbtnMinuman.setOnClickListener(this)
        binding.idbtnDessert.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.idbtn_makanan -> {
                val intent = Intent(this@KategoriActivity, ListKategoriActivity::class.java)
                intent.putExtra(Key.KEY_KATEGORI, Kategori("Makanan", DataMenu.dataMakanan))
                intent.putExtra(Key.KEY_NO_MEJA, getNoMejaPesanan)
                startActivity(intent)
            }
            R.id.idbtn_minuman -> {
                val intent = Intent(this@KategoriActivity, ListKategoriActivity::class.java)
                intent.putExtra(Key.KEY_KATEGORI, Kategori("Minuman", DataMenu.dataMinuman))
                intent.putExtra(Key.KEY_NO_MEJA, getNoMejaPesanan)
                startActivity(intent)
            }
            R.id.idbtn_dessert -> {
                val intent = Intent(this@KategoriActivity, ListKategoriActivity::class.java)
                intent.putExtra(Key.KEY_KATEGORI, Kategori("Dessert", DataMenu.dataDessert))
                intent.putExtra(Key.KEY_NO_MEJA, getNoMejaPesanan)
                startActivity(intent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}