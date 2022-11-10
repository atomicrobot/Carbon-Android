package com.atomicrobot.carbon.ui.lumen

import androidx.room.TypeConverter
import com.atomicrobot.carbon.data.lumen.dto.LightType

class LumenConverters {

    @TypeConverter
    fun fromOrdinal(ordinal: Int?): LightType? = ordinal?.let { LightType.values()[it] }

    @TypeConverter
    fun lightTypeToInt(type: LightType?): Int? = type?.ordinal
}
