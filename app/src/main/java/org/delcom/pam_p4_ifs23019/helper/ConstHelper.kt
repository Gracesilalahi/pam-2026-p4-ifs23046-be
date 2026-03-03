package org.delcom.pam_p4_ifs23019.helper

class ConstHelper {
    // Route Names
    enum class RouteNames(val path: String) {
        Home(path = "home"),
        Profile(path = "profile"),
        Desserts(path = "desserts"),
        DessertsAdd(path = "desserts/add"),

        DessertsDetail(path = "desserts/{dessertId}"),
        DessertsEdit(path = "desserts/{dessertId}/edit"),

    }
}