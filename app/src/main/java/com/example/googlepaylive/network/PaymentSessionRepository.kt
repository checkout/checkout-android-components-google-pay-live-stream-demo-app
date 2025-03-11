package com.example.googlepaylive.network

import com.checkout.components.sampleapp.BuildConfig
import com.example.googlepaylive.models.Address
import com.example.googlepaylive.models.AddressAndPhoneNumber
import com.example.googlepaylive.models.Currency
import com.example.googlepaylive.models.Customer
import com.example.googlepaylive.models.PaymentContextItem
import com.example.googlepaylive.models.Phone
import com.example.googlepaylive.models.Threeds
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
internal class PaymentSessionRepository
@Inject
constructor(
    private val paymentSessionApi: PaymentSessionsApi,
) {
    suspend fun createPaymentSession(
        token: String = "Bearer $SECRET_KEY",
        paymentMethodSupported: List<String> = listOf("card", "googlepay"),
        body: CreatePaymentSessionsRequest = createSessionObject(paymentMethodSupported),
    ): CreatePaymentSessionsResponse =
        try {
            val response = paymentSessionApi.createPaymentSession(token = token, body = body)

            if (response.isSuccessful) {
                response.body() as? CreatePaymentSessionsResponse.Success
                    ?: CreatePaymentSessionsResponse.Unknown(
                        "Successful response parsed error",
                    )
            } else if (response.code() == 401) {
                CreatePaymentSessionsResponse.Unauthorized
            } else {
                response.body() as? CreatePaymentSessionsResponse.Failure
            } ?: CreatePaymentSessionsResponse.Unknown("Failure response parsed error")
        } catch (e: Exception) {
            CreatePaymentSessionsResponse.Unknown(e.message ?: "Unknown error")
        }

    private companion object {
        private val SECRET_KEY = BuildConfig.SANDBOX_SECRET_KEY
        private const val QUANTITY = 1
        private const val UNIT_PRICE = 1
        private val PHONE =
            Phone(
                countryCode = "+49",
                number = "1234 567890",
            )
        private val ADDRESS =
            Address(
                addressLine1 = "addressLine1",
                addressLine2 = "addressLine2",
                country = "DE",
                state = "state",
                zip = "zip",
                city = "city",
            )

        private fun createSessionObject(paymentMethodSupportedList: List<String>): CreatePaymentSessionsRequest =
            CreatePaymentSessionsRequest(
                amount = (QUANTITY * UNIT_PRICE).toLong(),
                currency = Currency.GBP,
                reference = "REFERENCE",
                processingChannelID = null,
                billing = AddressAndPhoneNumber(ADDRESS, PHONE),
                shipping = AddressAndPhoneNumber(ADDRESS, PHONE),
                customer = Customer(
                    email = "random@email.com",
                    name = "customerName",
                    phone = PHONE,
                ),
                successUrl = "https://www.checkout.com/",
                failureUrl = "https://www.checkout.com/",
                enabledPaymentMethods = paymentMethodSupportedList,
                items = listOf(
                    PaymentContextItem(
                        name = "Go pro XYZ",
                        quantity = QUANTITY,
                        unitPrice = UNIT_PRICE
                    ),
                ),
                description = "Required for paying",
                threeds = Threeds(enabled = true),
            )

    }
}
