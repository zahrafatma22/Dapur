package com.example.dapur.pesanan

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.dapur.R
import com.example.dapur.databinding.ActivityPesananBinding
import com.example.dapur.key.Key

class PesananActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPesananBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Pesanan"

        binding.idbtnLanjut.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.idbtn_lanjut -> {
                val noMeja = binding.idedtNomermeja.text.toString()
                if (noMeja.isNotEmpty()) {
                    val intent = Intent(this, ListPesananActivity::class.java)
                    intent.putExtra(Key.KEY_NO_MEJA, noMeja)
                    startActivity(intent)
                } else {
                    binding.idedtNomermeja.error = "Tidak boleh kosong"
                }

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