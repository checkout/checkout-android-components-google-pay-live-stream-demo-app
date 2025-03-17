package com.example.googlepaylive.models

import com.example.googlepaylive.network.CreatePaymentSessionsResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

internal class CreatePaymentSessionsResponseJsonAdapter(
    moshi: Moshi,
) : JsonAdapter<CreatePaymentSessionsResponse>() {
    private val successAdapter: JsonAdapter<CreatePaymentSessionsResponse.Success> =
        moshi.adapter(
            CreatePaymentSessionsResponse.Success::class.java,
        )
    private val failureAdapter: JsonAdapter<CreatePaymentSessionsResponse.Failure> =
        moshi.adapter(
            CreatePaymentSessionsResponse.Failure::class.java,
        )

    override fun fromJson(reader: JsonReader): CreatePaymentSessionsResponse? {
        val peekedReader = reader.peekJson()
        peekedReader.beginObject()

        while (peekedReader.hasNext()) {
            when (peekedReader.nextName()) {
                "id" -> {
                    peekedReader.close()
                    return successAdapter.fromJson(reader)
                }

                "request_id" -> {
                    peekedReader.close()
                    return failureAdapter.fromJson(reader)
                }

                else -> peekedReader.skipValue()
            }
        }
        peekedReader.endObject()
        return null
    }

    override fun toJson(
        writer: JsonWriter,
        value: CreatePaymentSessionsResponse?,
    ) {
        when (value) {
            is CreatePaymentSessionsResponse.Success -> successAdapter.toJson(writer, value)
            is CreatePaymentSessionsResponse.Failure -> failureAdapter.toJson(writer, value)
            else -> writer.nullValue()
        }
    }

    internal companion object {
        val Factory: Factory =
            Factory { type, _, moshi ->
                if (Types.getRawType(type) == CreatePaymentSessionsResponse::class.java) {
                    return@Factory CreatePaymentSessionsResponseJsonAdapter(moshi)
                }
                null
            }
    }
}
