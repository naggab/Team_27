
package com.swtug.anticovid.view.addTestReport

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.VisibleForTesting
import androidx.navigation.fragment.findNavController
import com.swtug.anticovid.models.User
import com.swtug.anticovid.view.BaseFragment
import com.swtug.anticovid.R
import com.swtug.anticovid.Utils
import com.swtug.anticovid.models.TestReport
import com.swtug.anticovid.repositories.FirebaseListener
import com.swtug.anticovid.repositories.FirebaseRepo
import com.swtug.anticovid.repositories.PreferencesRepo
import java.time.LocalDateTime

class AddTestReportFragment: BaseFragment() {

    private lateinit var txtUserEmail: TextView
    private lateinit var txtUserName: TextView
    private lateinit var txtValidityInfo: TextView

    private lateinit var editTestDate: EditText
    private lateinit var editValidDate: EditText
    
    private lateinit var buttonAddTestDate: ImageButton
    private lateinit var buttonAddTestReportToFirebase: Button

    private lateinit var  radioPositive: RadioButton
    private lateinit var  radioNegative: RadioButton

    private lateinit var selectedDate: LocalDateTime

    private var isPositiveTest: Boolean = false

    private var currentUser: User? = null

    companion object {
        const val VALID_HOURS_NEGATIVE: Long = 48
        const val VALID_DAYS_POSITIVE: Long = 14
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_add_test_report, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser = PreferencesRepo.getUser(requireContext())
        initFields(view)
        setTestAndValidDate(LocalDateTime.now())

        initListeners(object : FirebaseListener {
            override fun onSuccess(user: User?) {
                setButtonsEnabled(true)
                Toast.makeText(requireContext(), getString(R.string.report_added_success),
                    Toast.LENGTH_LONG).show()
            }

            override fun onStart() {
                setButtonsEnabled(false)
            }

            override fun onFailure() {
                setButtonsEnabled(true)
                Toast.makeText(requireContext(), getString(R.string.error_firebase_communication),
                    Toast.LENGTH_LONG).show()
            }
        })

    }

    private fun addTestReportToFirebase(firebaseListener: FirebaseListener) {
        if(currentUser != null) {
            FirebaseRepo.addTestReport(getTestReportOfForm(), firebaseListener)
        } else {
            Toast.makeText(requireContext(), getString(R.string.error_no_user_logged_in), Toast.LENGTH_LONG).show()
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    fun setTestAndValidDate(testDate: LocalDateTime) {
        selectedDate = testDate
        editTestDate.setText(Utils.getStringFromDate(testDate))
        if(isPositiveTest) {
            editValidDate.setText(Utils.getStringFromDate(testDate.plusDays(VALID_DAYS_POSITIVE)))
            txtValidityInfo.text = getString(R.string.positive_test_valid_info)
        } else {
            editValidDate.setText(Utils.getStringFromDate(testDate.plusHours(VALID_HOURS_NEGATIVE)))
            txtValidityInfo.text = getString(R.string.negative_test_valid_info)
        }
    }

    private fun initListeners(firebaseListener: FirebaseListener) {

        buttonAddTestDate.setOnClickListener {
            pickDateAndTime()
        }

        radioNegative.setOnClickListener {
            isPositiveTest = false
            editValidDate.setText(Utils.getStringFromDate(selectedDate.plusHours(VALID_HOURS_NEGATIVE)))
            txtValidityInfo.text = getString(R.string.negative_test_valid_info)
        }

        radioPositive.setOnClickListener {
            isPositiveTest = true
            editValidDate.setText(Utils.getStringFromDate(selectedDate.plusDays(VALID_DAYS_POSITIVE)))
            txtValidityInfo.text = getString(R.string.positive_test_valid_info)
        }

        buttonAddTestReportToFirebase.setOnClickListener {
            addTestReportToFirebase(firebaseListener)
        }
    }

    private fun pickDateAndTime() {
        selectedDate = LocalDateTime.now()
        DatePickerDialog(requireContext(),
            { _, year, month, dayOfMonth ->
                selectedDate = LocalDateTime.of(year, month, dayOfMonth, 0, 0)

                TimePickerDialog(requireContext(),
                    { _, hourOfDay, minute ->
                        selectedDate = selectedDate.plusHours(hourOfDay.toLong())
                        selectedDate = selectedDate.plusMinutes(minute.toLong())
                        setTestAndValidDate(selectedDate)
                }, selectedDate.hour, selectedDate.minute, true).show()

        }, selectedDate.year, selectedDate.monthValue - 1, selectedDate.dayOfMonth).show()
    }

    private fun getTestReportOfForm(): TestReport {
        return TestReport(currentUser?.email ?: "",
                                editTestDate.text.toString(),
                                isPositiveTest,
                                editValidDate.text.toString())
    }

    private fun initFields(view: View) {
        txtUserEmail = view.findViewById(R.id.text_email_info)
        txtUserName = view.findViewById(R.id.text_user_name_info)
        txtValidityInfo = view.findViewById(R.id.validity_info)
        editTestDate = view.findViewById(R.id.text_test_date)
        editValidDate = view.findViewById(R.id.text_valid_date)

        buttonAddTestDate = view.findViewById(R.id.button_enter_test_date)
        buttonAddTestReportToFirebase = view.findViewById(R.id.button_add_test_report_to_firebase)

        radioPositive = view.findViewById(R.id.radio_test_positive)
        radioNegative = view.findViewById(R.id.radio_test_negative)

        txtUserEmail.text = currentUser?.email
        txtUserName.text = currentUser?.name + " " +  currentUser?.surname
    }

    private fun setButtonsEnabled(enabled: Boolean) {
        buttonAddTestReportToFirebase.isEnabled = enabled
        buttonAddTestDate.isEnabled = enabled
        radioPositive.isEnabled = enabled
        radioNegative.isEnabled = enabled
    }


}