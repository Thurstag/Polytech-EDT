package com.polytech.edt.model;

import com.polytech.edt.model.url.ResourceUrl;

import net.fortuna.ical4j.data.ParserException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Resource
 * <p>
 * Get all resources: http://ade.polytech.u-psud.fr:8080/jsp/webapi?function=getResources&projectId=2&data=5e3670a1af64840169d64367705be27e51e7ab85056895b426543d6e5ba99179
 * Get one resource: http://ade.polytech.u-psud.fr:8080/jsp/webapi?function=getResources&id=2128&projectId=2&data=5e3670a1af64840169d64367705be27e51e7ab85056895b426543d6e5ba99179&detail=4
 * </p>
 */
public class ADEResource implements ADELoadable<ADEResource> {

    //region Fields

    private static Map<String, Object> idsMap;

    private URL url;
    private int id;
    private String name;
    private String path;

    //endregion

    //region Constructors

    static {
        idsMap = new HashMap<>();
        String[] parts = "13,1777,1630,1838,1857,1739,1746,1953,2020,1732,1795,1823,2117,346,2139,2154,2180,2218,2219,1660,1691,1711,1762,1778,1809,1851,1888,1929,1952,1989,1998,2013,2047,2109,2142,2192,1741,1784,1885,1956,2152,2162,2164,1931,2023,2077,2085,519,2182,530,682,683,684,685,1667,1688,1697,1865,1993,2046,529,1695,1747,1820,1853,1898,1987,2007,2048,2057,2097,2028,2188,426,2107,2200,1684,1839,1999,1933,1948,2006,2186,543,1104,1702,1712,1797,1836,1854,1861,1874,1895,1951,1981,1983,1986,1994,2037,2051,2115,2118,2130,2136,2191,1723,1822,1911,1960,2054,2099,2122,2147,523,1637,1808,1832,1963,2025,2083,1650,1699,1810,2053,2055,2068,2121,2178,1662,1665,1713,1724,1828,1905,2070,2174,1669,1706,1709,1715,1796,1881,1995,2079,2172,675,676,677,678,1679,1870,1907,2021,2203,361,1792,1835,2101,2125,2144,249,250,251,1892,1955,2001,2018,2033,2163,2187,2091,1653,1700,1714,1798,1799,1802,1845,1882,1926,2022,2080,2102,2159,1718,1942,541,1094,1790,1906,1912,2012,2024,2160,1780,1827,1918,1947,1959,1961,2181,366,1081,1634,1685,1745,1779,1805,1872,1985,2116,2127,2204,1677,2157,1690,1698,1770,1807,1937,1949,2067,2081,2137,1924,1935,364,1975,2066,2185,2197,51,59,65,71,119,120,121,122,267,1097,1670,1708,1710,1816,1864,1867,1871,1919,1925,2035,2038,2076,2084,2153,2155,2158,2173,1738,1806,1996,1982,1815,2135,2005,1869,2026,2151,2014,1916,2043,1682,1825,1687,1693,1725,1772,1817,1846,1887,2042,2078,2128,2167,2177,2193,1769,1970,2034,1804,2049,2073,2011,2175,2064,2087,666,1085,1627,1692,1729,1883,1913,1978,2041,2165,1675,1696,1721,1787,2166,2184,1758,1990,2094,2129,2169,1765,1859,2086,2120,2124,2138,1789,1811,1866,1932,2069,2075,2082,2092,1834,2004,2056,2148,1850,2052,2059,2071,245,266,477,1641,1766,1884,1969,2039,2044,2058,1958,1672,1673,1716,1767,1773,1785,1824,1840,1844,1977,1984,1997,2015,2030,2096,2104,1783,2095,2100,2134,2146,2156,2171,1875,1979,1967,2050,1751,1880,1972,1904,1909,1917,2183,2195,2196,1965,645,646,1728,1914,1944,2074,2112,2113,1761,1786,1744,1812,1841,1876,1879,1976,1991,2008,2133,2149,2168,2202,1731,1829,1886,1962,1992,2003,2009,2114,2131,661,1775,1800,1813,1819,1831,1858,1908,2089,2119,2194,1856,1928,2061,1878,355,1921,1957,2098,2201,1646,1654,1735,1801,1862,2036,2060,2141,2143,2179,1664,1748,1763,1889,1890,1945,1966,1988,2040,2088,2145,2189,1894,2017,2031,1705,1756,1771,1852,1940,1943,1950,1973,2000,2123,1720,1939,2002,2126,2176,206,207,208,209,215".split(",");

        for (String part : parts) {
            idsMap.put(part, null);
        }
    }

    /**
     * Constructor
     *
     * @param id ID
     */
    public ADEResource(int id) throws MalformedURLException {
        this.id = id;
        this.url = new ResourceUrl(id).url();
    }

    //endregion

    //region Methods

    public String name() {
        return name;
    }

    public int id() {
        return id;
    }

    public String path() {
        return path;
    }

    @Override
    public String toString() {
        return id() + "";
    }

    @Override
    public ADEResource load() throws Exception {
        BufferedInputStream resource = fetchResource();
        byte[] contents = new byte[1024];
        int bytesRead;

        // Buffer to string
        StringBuilder builder = new StringBuilder();
        while ((bytesRead = resource.read(contents)) != -1) {
            builder.append(new String(contents, 0, bytesRead));
        }

        // Decode xml
        final Document document;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(builder.toString())));
        } catch (Exception e) {
            throw new ParserException("Failed to decode resource", 89, e);
        }

        // Check xml
        if (document.getFirstChild() == null) {
            throw new Exception("Malformed resource xml");
        }
        if (document.getFirstChild().getFirstChild() == null) {
            throw new Exception("Malformed resource xml");
        }
        if (document.getFirstChild().getFirstChild().getNextSibling() == null) {
            throw new Exception("Malformed resource xml");
        }

        // Get node
        NamedNodeMap map = document.getFirstChild().getFirstChild().getNextSibling().getAttributes();

        // Get data
        this.id = Integer.parseInt(map.getNamedItem("id").getNodeValue());
        this.name = map.getNamedItem("name").getNodeValue();
        this.path = map.getNamedItem("path").getNodeValue();

        return this;
    }

    /**
     * Method to fetch resource
     *
     * @return Resource
     */
    public BufferedInputStream fetchResource() throws IOException {
        return new BufferedInputStream(this.url.openStream());
    }

    /**
     * Method to fetch resources
     *
     * @return Resources xml
     */
    private static BufferedInputStream fetchResources() throws IOException {
        return new BufferedInputStream(new ResourceUrl(null, 0).url().openStream());
    }

    /**
     * Method to get all resources
     *
     * @return Resources
     */
    public static List<ADEResource> resources() {
        List<ADEResource> resources = new ArrayList<>();

        // Get data
        // TODO: Paralleling task
        for(String key : idsMap.keySet()) {
            try {
                resources.add(new ADEResource(Integer.parseInt(key)).load());
            } catch (Exception ignored) {

            }
        }

        return resources;
    }

    //endregion
}
