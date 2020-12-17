import akka.Done
import akka.actor.typed.ActorRef
import akka.cluster.sharding.typed.javadsl.ClusterSharding
import akka.cluster.sharding.typed.javadsl.EntityRef
import akka.cluster.sharding.typed.javadsl.EntityTypeKey
import akka.pattern.StatusReply
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.await
import kotlinx.coroutines.launch
import org.taymyr.akka.cluster.sharding.typed.javadsl.ClusterSharding.askWithStatus
import java.time.Duration

object Application {

    /**
     * Copy from https://github.com/taymyr/lagom-extensions/blob/v0.10.0/java/src/main/kotlin-2.13/org/taymyr/akka/cluster/sharding/typed/javadsl/ClusterSharding.kt#L19
     */
    suspend inline fun <M, RES> EntityRef<M>.askWithStatusLocal(timeout: Duration, noinline message: (ActorRef<StatusReply<RES>>) -> M): RES = this.askWithStatus(message, timeout).await()

    @JvmStatic
    fun main(args: Array<String>) {
        val sharding: ClusterSharding? = null
        val entityRef: EntityRef<String>? = sharding?.entityRefFor(EntityTypeKey.create(String::class.java, "item"), "id")
        GlobalScope.launch {
            entityRef?.askWithStatus<String, Done>(Duration.ofSeconds(1)) { "" }
            entityRef?.askWithStatusLocal<String, Done>(Duration.ofSeconds(1)) { "" }
        }
    }

}