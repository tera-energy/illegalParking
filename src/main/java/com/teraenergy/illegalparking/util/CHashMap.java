package com.teraenergy.illegalparking.util;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public class CHashMap<K,V> extends HashMap<K,V> implements Map<K, V>, Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    public CHashMap(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public CHashMap(int initialCapacity) {
        super(initialCapacity);
    }

    public CHashMap() {

        super();
    }

    public CHashMap(Map<? extends K, ? extends V> m) {
        super(m);
    }

    /**
     * value as boolean
     *
     * @param key
     * @return
     */
    public Boolean getAsBoolean(Object key) {
        V value = super.get(key);

        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof Number) {
            return ((Number) value).doubleValue() > 0 ? true : false;
        } else if (value instanceof String) {
            try {
                return Double.parseDouble(value.toString()) > 0 ? true : false;
            } catch (Exception e) {
                try {
                    return Boolean.valueOf(value.toString());
                } catch (Exception e2) {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public Boolean asBoolean(Object key) {
        return getAsBoolean(key);
    }

    /**
     * value as short
     *
     * @param key
     * @return
     */
    public Short getAsShort(Object key) {
        V value = super.get(key);

        if (value instanceof Number) {
            return ((Number) value).shortValue();
        } else if (value instanceof String) {
            try {
                return Short.parseShort(value.toString());
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * value as int
     *
     * @param key
     * @return
     */
    public Integer getAsInt(Object key) {
        V value = super.get(key);

        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return Integer.parseInt(value.toString());
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public Integer asInt(Object key) {
        return getAsInt(key);
    }

    /**
     * value as long
     *
     * @param key
     * @return
     */
    public Long getAsLong(Object key) {
        V value = super.get(key);

        if (value instanceof Number) {
            return ((Number) value).longValue();
        } else if (value instanceof String) {
            try {
                return Long.parseLong(value.toString());
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * value as float
     *
     * @param key
     * @return
     */
    public Float getAsFloat(Object key) {
        V value = super.get(key);

        if (value instanceof Number) {
            return ((Number) value).floatValue();
        } else if (value instanceof String) {
            try {
                return Float.parseFloat(value.toString());
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * value as double
     *
     * @param key
     * @return
     */
    public Double getAsDouble(Object key) {
        V value = super.get(key);

        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.parseDouble(value.toString());
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * value as string
     *
     * @param key
     * @return
     */
    public String getAsString(Object key) {
        V value = super.get(key);

        if (value instanceof List || value instanceof Map || value == null) {
            return null;
        } else {
            return value.toString();
        }
    }

    public String toString(Object key) {
        return getAsString(key);
    }

    /**
     * value as date
     *
     * @param key
     * @return
     */
    public LocalDate getAsDate(Object key) {
        V value = super.get(key);

        if (value instanceof LocalDate) {
            return (LocalDate) value;
        } else if (value instanceof String) {
            String v = (String) value;
            if (v == null || v.length() == 0)
                return null;

            try {
                return LocalDate.parse(v, DateTimeFormatter.ISO_DATE);
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public LocalDate asDate(Object key) {
        return getAsDate(key);
    }

    /**
     * value as list
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<V> getAsList(Object key) {
        V value = super.get(key);

        if (value instanceof List) {
            return (List<V>) value;
        } else {
            return null;
        }
    }

    public List<V> asList(Object key) {
        return getAsList(key);
    }

    /**
     * value as map
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public CHashMap<K, V> getAsMap(Object key) {
        V value = super.get(key);

        if (value instanceof Map) {
            return (CHashMap<K, V>) value;
        } else {
            return null;
        }
    }

    /**
     * check is list value
     *
     * @param key
     * @return
     */
    public boolean isList(Object key) {
        V value = super.get(key);

        return value instanceof List;
    }

    /**
     * check is map value
     *
     * @param key
     * @return
     */
    public boolean isMap(Object key) {
        V value = super.get(key);

        return value instanceof Map;
    }
}
