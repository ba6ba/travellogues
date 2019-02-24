package modules.plan.Planning

import com.anychart.anychart.PertDataEntry


 class CustomPertDataEntry : PertDataEntry {
    internal constructor(id: String, duration: Int, name: String, fullName: String) : super(id, name, fullName) {
        setValue("duration", duration)
    }

    internal constructor(id: String, duration: Int, name: String, dependsOn: Array<String>, fullName: String) : super(id, name, fullName, dependsOn) {
        setValue("duration", duration)
    }
}