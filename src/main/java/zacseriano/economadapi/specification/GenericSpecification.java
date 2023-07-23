package zacseriano.economadapi.specification;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unchecked")
public class GenericSpecification<T> {

    static final String caractereOperadorLike = "%";

    public static Filtro builderFiltro(QueryOperator operador, String chave, Object valor) {
        return Filtro.builder()
                .operador(operador)
                .chave(chave)
                .valor(valor)
                .build();
    }

    public static <T> Specification<T> verificarSpecification(Specification<T> atual, Specification<T> nova, String condicao) {
        if(atual != null) {
            if(condicao.equals("and")) {
                return atual.and(nova);
            } else {
                return atual.or(nova);
            }
        } else {
            return nova;
        }
    }

    protected static <T> Specification<T> createSpecification(Filtro filtro) {
        switch (filtro.getOperador()) {
            case IN:
                return in(filtro.getChave(), filtro.getValores());
            case EQUAL:
                return equals(filtro.getChave(), filtro.getValor());
            case NOT_EQUAL:
                return notEquals(filtro.getChave(), filtro.getValor());
            case LESS_THAN:
                return lessThan(filtro.getChave(), filtro.getValor());
            case GREATER_THAN:
                return greaterThan(filtro.getChave(), filtro.getValor());
            case LESS_THAN_OR_EQUAL:
                return lessThanOrEqualTo(filtro.getChave(), filtro.getValor());
            case GREATER_THAN_OR_EQUAL:
                return greaterThanOrEqualTo(filtro.getChave(), filtro.getValor());
            case LIKE:
                return like(filtro.getChave(), filtro.getValor());
            case IS_NULL:
                return isNull(filtro.getChave());
            default:
                throw new RuntimeException("Operation not supported yet");
        }
    }

    private static <T> Specification<T> like(String key, Object value) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            return builder.like( builder.lower(root.get(key)), caractereOperadorLike+value.toString().toLowerCase(Locale.ROOT)+caractereOperadorLike);
        };
    }

    private static <T> Specification<T> isNull(String key) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            return builder.isNull(root.get(key));
        };
    }

    private static <T> Specification<T> equals(String key, Object value) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            return builder.equal(root.get(key), value);
        };
    }

    private static <T> Specification<T> notEquals(String key, Object value) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            return builder.notEqual(root.get(key), value);
        };
    }

    private static <T> Specification<T> in(String key, Collection<Object> values) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            return builder.in(root.get(key)).value(values);
        };
    }

	private static <T> Specification<T> greaterThan(String key, Object value) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if(value instanceof LocalDate) {
                return builder.greaterThan(root.get(key), (LocalDate) value);
            }

            return ((Specification<T>) gt(key, value)).toPredicate(root, query, builder);
        };
    }

    public static <T> Specification<T> greaterThanOrEqualTo(String key, Object value) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            return ((Specification<T>) greaterThan(key, value).or(equals(key, value))).toPredicate(root, query, builder);
        };
    }

    private static <T> Specification<T> lessThan(String key, Object value) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if(value instanceof LocalDate) {
                return builder.lessThan(root.get(key), (LocalDate) value);
            }

            return ((Specification<T>) lt(key, value)).toPredicate(root, query, builder);
        };
    }

    private static <T> Specification<T> lessThanOrEqualTo(String key, Object value) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            return ((Specification<T>) lessThan(key, value).or(equals(key, value))).toPredicate(root, query, builder);
        };
    }

    private static <T> Specification<T> gt(String key, Object value) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            return builder.gt(root.get(key), (Number) value);
        };
    }

    private static <T> Specification<T> lt(String key, Object value) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            return builder.lt(root.get(key), (Number) value);
        };
    }

    protected static <T> Specification<T> startsWith(String key, Object value) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            return builder.like( builder.lower(root.get(key).as(String.class)), value.toString().toLowerCase(Locale.ROOT)+"%");
        };
    }

    protected static <T> Specification<T> startsWithCpfCnpj(String key, Object value) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            String val = value.toString().replace(".", "").replace("-", "").replace("/", "").toLowerCase(Locale.ROOT);
            var replacePonto = builder.function("replace", String.class, builder.lower(root.get(key).as(String.class)), builder.literal("."),
                    builder.literal(""));
            var replaceTraco = builder.function("replace", String.class, replacePonto, builder.literal("-"),
                    builder.literal(""));
            var replaceBarra = builder.function("replace", String.class, replaceTraco, builder.literal("/"),
                    builder.literal(""));

            return builder.like(replaceBarra,val+"%");
        };
    }

    protected static Filtro criarFiltroNulo(String chave) {
        return Filtro.builder()
                .operador(QueryOperator.IS_NULL)
                .chave(chave)
                .build();
    }

    protected static Filtro criarFiltro(QueryOperator operador, String chave, Object valor) {
        return Filtro.builder()
                .operador(operador)
                .chave(chave)
                .valor(valor)
                .build();
    }

    protected static Filtro criarFiltroValores(String chave, List<Object> valores) {
        return Filtro.builder()
                .operador(QueryOperator.IN)
                .chave(chave)
                .valores(valores)
                .build();
    }
}

