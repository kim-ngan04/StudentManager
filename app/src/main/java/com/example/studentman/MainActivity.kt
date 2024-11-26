package com.example.studentman

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var students: MutableList<StudentModel>
    private lateinit var adapter: StudentAdapter
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        students = mutableListOf(
            StudentModel("Nguyễn Văn An", "SV001"),
            StudentModel("Trần Thị Bảo", "SV002"),
            StudentModel("Lê Hoàng Cường", "SV003"),
            StudentModel("Phạm Thị Dung", "SV004"),
            StudentModel("Đỗ Minh Đức", "SV005"),
            StudentModel("Vũ Thị Hoa", "SV006"),
            StudentModel("Hoàng Văn Hải", "SV007"),
            StudentModel("Bùi Thị Hạnh", "SV008"),
            StudentModel("Đinh Văn Hùng", "SV009"),
            StudentModel("Nguyễn Thị Linh", "SV010"),
            StudentModel("Phạm Văn Long", "SV011"),
            StudentModel("Trần Thị Mai", "SV012"),
            StudentModel("Lê Thị Ngọc", "SV013"),
            StudentModel("Vũ Văn Nam", "SV014"),
            StudentModel("Hoàng Thị Phương", "SV015"),
            StudentModel("Đỗ Văn Quân", "SV016"),
            StudentModel("Nguyễn Thị Thu", "SV017"),
            StudentModel("Trần Văn Tài", "SV018"),
            StudentModel("Phạm Thị Tuyết", "SV019"),
            StudentModel("Lê Văn Vũ", "SV020")
        )

        listView = findViewById(R.id.list_view_students)
        adapter = StudentAdapter(this, students)
        listView.adapter = adapter

        // Register for context menu
        registerForContextMenu(listView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                startActivityForResult(
                    Intent(this, AddStudentActivity::class.java),
                    ADD_STUDENT_REQUEST
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val student = students[info.position]

        return when (item.itemId) {
            R.id.action_edit -> {
                val intent = Intent(this, EditStudentActivity::class.java).apply {
                    putExtra("student_position", info.position)
                    putExtra("student_name", student.studentName)
                    putExtra("student_id", student.studentId)
                }
                startActivityForResult(intent, EDIT_STUDENT_REQUEST)
                true
            }
            R.id.action_remove -> {
                val removedStudent = students[info.position]
                students.removeAt(info.position)
                adapter.notifyDataSetChanged()
                showUndoSnackbar(removedStudent, info.position)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                ADD_STUDENT_REQUEST -> {
                    data?.let {
                        val name = it.getStringExtra("student_name") ?: return
                        val id = it.getStringExtra("student_id") ?: return
                        students.add(StudentModel(name, id))
                        adapter.notifyDataSetChanged()
                    }
                }
                EDIT_STUDENT_REQUEST -> {
                    data?.let {
                        val position = it.getIntExtra("student_position", -1)
                        val name = it.getStringExtra("student_name") ?: return
                        val id = it.getStringExtra("student_id") ?: return
                        if (position != -1) {
                            students[position] = StudentModel(name, id)
                            adapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    private fun showUndoSnackbar(removedStudent: StudentModel, position: Int) {
        Snackbar.make(listView, "Đã xoá sinh viên", Snackbar.LENGTH_LONG)
            .setAction("Hoàn tác") {
                students.add(position, removedStudent)
                adapter.notifyDataSetChanged()
            }
            .show()
    }

    companion object {
        const val ADD_STUDENT_REQUEST = 1
        const val EDIT_STUDENT_REQUEST = 2
    }
}