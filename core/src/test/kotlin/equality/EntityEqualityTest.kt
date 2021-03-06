package equality

import com.gitlab.kordlib.common.entity.Snowflake
import com.gitlab.kordlib.core.entity.Entity
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

val ids = generateSequence { Random.nextLong() }.distinct().iterator()
fun randomId() = Snowflake(ids.next())

interface EntityEqualityTest<T : Entity> {

    fun newEntity(id: Snowflake): T

    @Test
    fun `Entities with the same id are equal`() {
        val id = randomId()
        val a = newEntity(id)
        val b = newEntity(id)

        assertEquals(a, b)
    }

    @Test
    fun `Entities with different ids are not equal`() {
        val a = newEntity(randomId())
        val b = newEntity(randomId())

        assertNotEquals(a, b)
    }

    companion object {
        operator fun <T : Entity> invoke(supplier: (Snowflake) -> T) = object : EntityEqualityTest<T> {
            override fun newEntity(id: Snowflake): T = supplier(id)
        }
    }
}