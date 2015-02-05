package au.org.emii.aatams

class ReceiverEventQueryBuilder extends QueryBuilder {
    String getViewName(filterParams) {
        return "receiver_event_view"
    }
}
