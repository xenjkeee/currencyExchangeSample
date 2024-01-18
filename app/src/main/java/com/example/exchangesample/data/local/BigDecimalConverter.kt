package com.example.exchangesample.data.local

import androidx.room.TypeConverter
import java.math.BigDecimal

object BigDecimalConverter {
    @TypeConverter
    fun fromString(value: String?): BigDecimal =
        value?.let { BigDecimal(it) } ?: BigDecimal.ZERO

    @TypeConverter
    fun toString(value: BigDecimal?) = value?.toString() ?: "0"

}