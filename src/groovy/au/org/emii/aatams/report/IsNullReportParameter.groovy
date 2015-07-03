package au.org.emii.aatams.report

class IsNullReportParameter extends ReportParameter  {
    String getTemplate() {
        return "/report/filter/isNullTemplate"
    }

    String getRestrictionName() {
        return "isNull"
    }
}
