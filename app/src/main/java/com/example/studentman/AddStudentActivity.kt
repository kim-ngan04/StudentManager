package com.example.studentman

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        title = "Add New Student"

        findViewById<Button>(R.id.btn_save).setOnClickListener {
            val name = findViewById<EditText>(R.id.edit_student_name).text.toString()
            val id = findViewById<EditText>(R.id.edit_student_id).text.toString()

            if (name.isNotBlank() && id.isNotBlank()) {
                setResult(RESULT_OK, Intent().apply {
                    putExtra("student_name", name)
                    putExtra("student_id", id)
                })
                finish()
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}