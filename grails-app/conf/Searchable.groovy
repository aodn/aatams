searchable {
    compassSettings = [:]
    defaultExcludedProperties = ["password"]
    defaultFormats = [:]
    defaultMethodOptions = [
        search: [reload: false, escape: false, offset: 0, max: 10, defaultOperator: "and"],
        suggestQuery: [userFriendly: true]
    ]
    mirrorChanges = true
    bulkIndexOnStartup = true
    releaseLocksOnStartup = true
}

environments {
    test {
        searchable {
            // use faster in-memory index
            compassConnection = "ram://test-index"
        }
    }
}
