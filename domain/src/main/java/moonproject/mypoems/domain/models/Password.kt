package moonproject.mypoems.domain.models

data class EncodedPassword(val value: String) {

    override fun equals(other: Any?): Boolean {
        return other is EncodedPassword && this.value == other.value
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }
}

data class DecodedPassword(val value: String) {

    companion object {
        val EmptyPassword = DecodedPassword("")
    }
}
