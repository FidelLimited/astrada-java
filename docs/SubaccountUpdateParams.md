

# SubaccountUpdateParams


## Properties

| Name | Type | Description | Notes |
|------------ | ------------- | ------------- | -------------|
|**configurations** | [**CreateSubaccountConfigurations**](CreateSubaccountConfigurations.md) |  |  [optional] |
|**name** | **String** |  |  [optional] |
|**verificationPolicy** | [**VerificationPolicy**](VerificationPolicy.md) | Set or clear fields of the subaccount&#39;s card verification policy. Fields you send replace those fields and fields you leave out keep their value; the policy as a whole cannot be sent as &#x60;null&#x60;. Customers may set any level above LOW (MEDIUM, HIGH, HIGHEST), send a null level to clear the override, toggle the &#x60;sandbox&#x60; test flag, and set &#x60;failedAttemptLockout&#x60;. LOW is internal-only and is rejected on this endpoint.  |  [optional] |
|**enrollmentPolicy** | [**CustomerEnrollmentPolicy**](CustomerEnrollmentPolicy.md) |  |  [optional] |



