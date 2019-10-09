import com.bsiag.commons.Base64Utility;
import com.bsiag.scout.shared.proxy.http.HttpProxyHandlerRequest;
import com.bsiag.scout.shared.proxy.http.HttpProxyHandlerResponse;
import com.neotys.AdunoVisecaProtocol.common.NeoLoadHttpInputstream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;


import com.neotys.AdunoVisecaProtocol.common.NeoLoadMessageEncoder;
import com.neotys.AdunoVisecaProtocol.datamodel.NeoLoadSoapResponse;
import com.neotys.AdunoVisecaProtocol.datamodel.NeoloadSoapRequest;
import com.thoughtworks.xstream.XStream;
import org.junit.Test;

public class CustomDecoder {

    private String input1="<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "<SOAP-ENV:Body>\n" +
            "  <request version=\"3.7.0\" format=\"de_CH\" language=\"de_CH\" service=\"com.bsiag.scout.shared.services.common.sql.ISqlService\" operation=\"select\"/>\n" +
            "  <data>eAFVU7trFEEc/mVj7s4EJA9iI8KClRDmAoKvkCJuNtzpenfsJkZMsU72xrsNs4+bmV02FiltUhhQC0HQws5g45/ho7XUwlLBUiud2YdnptjH7/37vm+Of8AUZ3DNiwK0y308QNyLEoH4EDPSRzGLsn00FCJGLfnoqd8WDvuUMJuMEsIFlEeDxg7UAhezgTQu7Fh7OMVNisNBs7u7RzyxYkEjcGnkYUoEzBf+RPi0aeU26Z8O3IAIvI4F/hdxssJM4IaUFwkjOADNAmmKYsKw8KPwZJYjmB8OVnZgNnBjzLCsTdjmfqymnv9/QINizmX/c4HLCUt9j7RDGfoAeyR3dWSq6nbKgtOBmxLGZa/ckMUJq5ZFalRULnv06e7LWX6RagBZLCHSBDxxTMs0NvWe3TVMx0G9NdvsbLpb7fUlvTyVa8tQZn3D7t6Wnq7tuMaaY+rGUv69ZVQ19O2WaZu6bqDtrn1rw+pu53mrlR+VhdY666qDKuJ2bH1Vv+5hTjpMT9QWdTnfhBSBPhaB/AqikKNhRPtyXdS508M+K6me0GDSUlyHY1wkdWFCqUJXwNx/5FfY1iVwmCY5jpNZLKBWjBDLxmeUVgr4rEgydvjt6MPjC180mLgJU3lWxmB2HNRJgl3CHh0/Oz/z9OthBTHUmmlPVisClbJQIZSDX3P3Xyz//j4JtTY0hpgPvahPLKhLpYeC7ZfUTpNMkFBRy0tLQzGa4EFFfo17zI9F6a2nmPk4LH6z+I88AjSjJa+EfPfzpEUVu5jF6rUgd+aEyqsgNVPqr1i6wOjN2c9v33+897zaR0urXXJhFWp+9fP11Ycr727kQakqWxdweUzcidtbipmjgk7ERxS1nRF1CpELmLqErqDlv+diRQw=</data>\n" +
            "  <info origin=\"172.21.12.205\"/>\n" +
            "</SOAP-ENV:Body></SOAP-ENV:Envelope>";

    private String input2="<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "<SOAP-ENV:Body>\n" +
            "  <response status=\"OK\" type=\"Object[][]\"/>\n" +
            "  <data>eAFtkj9Lw0AYxt8GWrBFKf4BFycnl7vJxYKLHVopKioiZAiX9GxTc8l5d6lxcXRx0EFnv0BR9Au4izi6OOjQ1U/g4iVpbaTNcOTlfu/7vM/D9b4hLwWsOQFDtnRJC0knCBWSbSJoE3ERRGeorRRHNX3sxGWN+E2Pil0qeeBLCoPPgIIJC8xyPJf6aitQ7pHrEOVqRsGO2dAKOFHAiQJOFbCkous6VGJ9zwIfp+1+ph3XN8ZGVhpQYFaTKKJgttEhXYI94rfwtt2hjtK3JWbRyKE81lewkEH22yI4JbZHNVVkFqOKVPWcEzgHw4QpZmn7B0TopefMTNvf5Hlm6VT0xtL1W9VQJBYVlDNsI/BblYiHYuB7UrJD3yj1jcZ9o0m+m0+rj7vv9VMDIOJx8lpk3kwXRXEEaLDo4vPPXv/442UI5jQ4MPSPu347vCvLFW/IGfo1zMSBplRi5bJ//Xq1/GlAbhPyXeKFNBJQHkFbIbOpuOjdLpVuvi6HkwCK90rGuU4nT6T4sM55GNclXRuj3xzPUAD2Lzi+3Jk=</data>\n" +
            "  <info origin=\"10.237.204.22\"/>\n" +
            "</SOAP-ENV:Body></SOAP-ENV:Envelope>";

    private String input4="<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "<SOAP-ENV:Body>\n" +
            "  <response status=\"OK\" type=\"RemoteFile\"/>\n" +
            "  <data>eAF1e3VYXM/S5iDB3QJBgjtD0OASPLhbcAbXmcHd3YI7wR1CcHcJbsHdneASYMnv3vvtt/vt9h/nOd1d1dX11nnOzOl6q/wE8AYCBvCZ2NsCjSGWRuZAiIm9ExQIsTACg0yBDmB7VzegBRTqAJR+vSj97Uob2ZnagMAqIIiDvR0EBPh3gwUg6ALwbQ1MbCxBdlAFe6ilmaWJEdTyVQYKUNKVe7XA+o8F1n8ssP7LAisEBHa2NAFBWF/nbe3tWP+lbvff1FllxP7HkvxyAARbA1MjqBEUgCNnZeRsxGpjZGfOqmhsBTKBvs6i2hqAXE1ADn/tQwH4/01EzQJs72JkbAN6lUKxNbAFQY3EX9dxBHgDYHUByLYGr+5rGIFfN42r+9/U/mtlPFuDV1RedwyxtDMXdwL/4yIUgPXfZOXs7cz5XR2cwP/2+/+F7H/8Bv7Lb+D/9Bv4//LbtIbru8ovGRdYAMDV4S/yr8Hj/v8E7/82YWZpAwKqgGztoSDJ19t/xw0GFoAqA8B8DdwrUq+RkwPZmUMtPgPevI6ATXQASH+RtIRAIZ8BGLYGNkYQqLy96WtsQaYyANxXLIzAUFXo60XJHmL5F205APqr5uvjAwFBFYxe4f0/AqQKBb/Cxq8LwPpr0NYB/IojyPRvAKCv8H/6l/K/NqLm5gD6GxUUub97MbUE/7vzuiEbexMjm/+9shPU0oZV7p+xfz0Ydq92/5F+xQgWL/avpyE1dAwwAACMjbN6JtbfASjgjbqaJAvPa5ReDZdfvr1DQFJb/zeuLN9cF0+89CBLPEk3dfVWL3D99nN8C1EssjXxPNOnwPulrZhGZpeazIZ6NSxkGULVHAL4rfmlx7Ndc5vZ1buP24xvUX0TxJXz4ePg6Vv41DlklnDGAhBfWycvovDLg+26b1Wo+zyNh3XdaAWXb+jOYsrVza8xnfcfVzvWFlaKK54ehoVfpsfXBPYCBshItq+xjb9X/Q4OvfXzMbWvGJ8/F6tz9/HpcFubh1pW6SytrFQ8exzlustS/G4Gkpb/Rni5zwbERnkH4fVEBwD84GobzIw80EOFycCxfXeYX4xFb6ye1o/Pex6I78sYJV4OqGLUWfYp7n1sbTr04WYNpnSup3RsC+oO7zMPglVhdJnfnHedyE1zm8Y2KNq+SpWdqU6ZYYpxvn8SlBd9uTeLRjoj4yhbK0RyB2pPoFp8d0bcrCPCHg1YuvG+lAtNJYBhGb5GxvW5rGrOH01ffzfZ36sRSyNO8xNFfDSei4pOIlxeNZ9kNV/6u224gOJzBWMfUf7VziZ7ASsGmSHBHx920DfyYAQxRHZ4hNiYzVUdp5Pk9YYLXC9uB8oXFp/AHMEnqt+qwakBZt7MzznDeQQWOJJGrAdzRvak720bVxxvJfWQX+xw+YzND1tm4+Y/0/wJqyUVKX5YfSe2dsd82Vo3iAjYb7yBA7y8aQYaEevxdI71hMDtr2P02adlQPdG6cRtpdfTCRqoiMlyGx81z9cmaVNIUl/Cn6VrZW3FPzLPOa5PiL3v1UKn2Jl55vodsIqlswx/Me73cK57zPHCN/3ugRtPp2fg9+xDsZHQIQE4Q2MCZx7uqLXgek7VlgZgDp3xwhTOxayroJD9kzIq7Bxj++tFaW1hE4sXarFyTZ9NVS+yO4X5cib9fM9ECLYlaNsAiGz7qPY+fUnMw50usvL/9MSMwCCa7n360TtsH/fFjupAFccnlz3cYCphDb6jzgd58SZUmJD8we8opW0d9aBjtGxdtTSLNvQlfPeEOfvDm+uZw5bKEAJP8J+DRyFKX0dYM6I4iOuThZf8N5bfexa+O1dKsV9Iw4BouwXsI7LZohoEI7qglOWwmtO7wF8w0yAv8bPvX5OKx8kne+4hFu++M4avT6PO6lzC71g3C38ARTZ+UIvm5SP2N1snOzhlIqsXkxpxVXxzZX2oY8y7N3xTYJ33pk1vfuHeizQ6qA7xy/wyhvlxVWxMC9vWliH4NrntuknSvF1Bzkz0JeyKYDK/mPBCtf1H5PXG0hA6fMUzWl1uRCm3ev3uOo1QplBtulWhHTv0JxjO4+Nh9VdevcGd3CYBqe+lfmYsBS5i4XrhC2JszS6juluPkXQINtt+uLUb2k9FwMvmCUcUtcGCjCI32efiW48jXV3dumtQraonjI1vnmHV98h2+sqXn41Hw5uVkOWiytk5ZHftAYk+dkQvjezRYV5WWZ4P7nsH3Ykv8BKF+Dz8M23PUuNXta6AsCQG593kPpHRr7MX/MSWgTTTtYPzvUUWrotX8UEnH498yjvuh3pujgTdNJp/PIXwLOMFI/yhrAjhUQYf4ErkcIknPOQ3GNaDYjWSp3ozGpTf4o6ca2ZuvlP5EhDB/yJqszxNaaqLshCNAjOHh05RH/G2niroxMGoYZkqwV0tI9aMqhJ34sxfcT5gvdfpePON/IwoUrPABLVZLsK3FAJnvlACVotcFJNNHTQcYhX0trTibq15oatq9db8BpWpJWSftO3WePBqELiSdPDDb8S+IbcG3LkzqkPRG1fEEh7pgUiPsMz3XjqtvWG5UjTNXv61YRn0T3TJ3dEJWuuxs1aOwKrte0Jo/rDgOZEkXhwlza8tHOEzuHD3eRO15PKhRkftMZd297dMZv3rZI2BynOOvTA9Ax7+xfVkYynr2pPv+AS2nDiJtpZmYSjlJEZ6fRRucKaBlkFFjfNw2UeOdI2WC3+snisSPDAc5cHrhkNB0cRfc2xPyJoTcJ5Bv2QxqUNeGKJHa5oOkaM9yuoc610hP7a/d1Gi8OPhlHXEduMoWH1NsXP5HvBNlX5jXas+7Cel0PF06YVwv/aMZUXrD3rR6SI8y50FoNo0VX5y/onMD9frfhHmfdLk9fq+j+19Sh3j6T8I1jbxBm7V3mvMFbssBRL0nKMfonRcK7PQ5zm0nQIjx+ljzIgxUTmWC6ZIoKZOlvX6KKO/Pk8ft+GltV1YZHVwCewRQ10HgkPdgZM/xFr94fK9fOibjaJe7CKkF8C5Gna8NZWyci6u8y7I7I1M0WEhpA91KWHuzwFClIhqXXVqMvuEWd+RSXXbwtJ5aOd36xFc2NKOzMNkfAi8L+J8NPQNaBQeg1QN3nyOhVbPh5cxDGrJ9HZxKM+hOZ1olVP88FKN4vxS3jl7vwt61inlJDHSEftN1b0XofvgK+d+m9EfSCmlD3H1orn1avJrCuPEmBkz5ERXKE8ftwzsFT9riY6y2NiCfLBrXp5MPTRGNok3h3rGNMGhm0wL/Kl1PMXVBXLtVHLGAysHW1O3MnsR61RKqGM2eOw/KLVK2A2blha7MuNzCYYbFX/9HXXJY1qrIqPxbSFbsV4g3voo7le+D9+JSwSXKcadiLxKYCEFinpurtjbzQ4VqPVLx5dJA2SfDHDIn2tW3Ze4fCr8EvKNkFa8p+KRmDWyIWmbbNR5MtT1E3s6H2kk355vl9VsnwuvCPosNQkktWO04S05xzKNKYImnsOLpZtoIDJ/Ii0Q3Sg9MeJsBIJTK6gsrUEfErXXPl1tIcHa965u00EO0nvJXN3m02tx7l3SjBllZsfv2NFCytld9nOqBmXyoKYPMWw7mZeEJGi7C89BvUAK52XDd6A8SYKMZmrjr03z1ox0IrIY81fV9KZ21FvEBWIVaN2cFLn1YsSFsjr6PupyluRcKxjFhxv+zoypLZUeq5+f8BthqLmlw/PWdO1POadWeNWD+/1/ZR2Q6NqRE6D3lZmWHtnP+cmq9n9rb3nmu+VBoovHDCA5dRBFG9MhiJYvrSN7QrZpiaQZBVMKQQWZW1c1q96eReqv5JzuhtjsnV48D3763NBRtXdqHvCGOa9riXnqlyCTxBKT1CTBWZ05F/Yf3dC25mO1B9WwOpYv8TjSJLLjBZgeR0V7Ry/f92c28B7ECCSb9y4EVLdZT3wqARAf5bzTCOExWXKJ+Ntk8079GYHniJ7nxXkV9pgDwc/bZtastUxex/PvhaQvKaNkj99uyY+HxyUtX5HQevgPXYGKtdN/SHIdXXovpNwOn7h1LKfeDrtFmF2utBq0j2+IVnXSv3yJwrff1DBUkWC+W5R+/qNiqio7xzrsCWmVj+mW5quSa3+LhVjrtyWy2/s9//09XVXvwW8GKwaKAy3WuxFWlALO91eimBO/YsG2jJHKiF+zMD0sFAjLKTcZVMl6Z6k3ZWOw30nB+C228ufd34oztGTeOwpZmtJCOe7zOb/dXh+vVFkfeK/YvBAvPK5NTL0ELzw3INoh8hPRdhX8ySJAkUyRw/ZDwOIf5nCodRzq0OSEgyHM03t7/emHs3BxX6wm5oxVUu+U5MD87a+qhPDBP+ileL6qxfgciENnn6UrsWgljLVLP8s1L8pIKIWGqOGXKMVKhgjNcTBB9IZtKtMzx6JGVuwtDry3xk/TD7ylEqAkfdzjsu7p4/1C9gh+H1wMfbV8RLvTgY9C05lt19Z1jOYmiHrxbYqfNAxQtFdIauLzCmK5Rij4n11oTAfOl/oT0YS4EU8u8nBqO5ERCidJS+QDqIy8udMuat6nZqBJXaPdoSmE5UkieEbNgufNmq9bSRuEbKncSOquwE7uPxwDQZ89J4dvCeNL3rRGHAB+iqKDl5N2KVvh0ZPsquk/sqLSOYbSKPXExicTtc9bUgdTFOHp9+ISuaXyFiJF7HFCp6gc0yhD7S29WOONFpJn+T87eknlSAQtBy8ar1Fzb6ImFkpwokh/Nz0Tonb586juEIlMHZPi6rn+FDRJFERuQ0955bGvLy6e3llbEYw8qY/4kFFPZeXj0k1QjR3I/xkJ88HCVyL1o4MZzrIujUOIFjwOP5LHCJ3gOB2asr/4cS1y5NnM8mc5srjpEM9uPlqlNk2xnRx+ST3/s84SNosWWTGxma3hsjzbBvwgzSWVAKm7jL5QxYXxWHu8SPIyhtOMxyvs++6cFEBBPTBDJCaVo4WrrmAdnu2hd44p/qa+sJ4yQ2nuMlI7Im2R0uGQL0aySYXjhVJok0y1P5luHkTrUJL6BzXS3dPoa7ErfzYWqWE/ifLdEl393ZnHfij+MLxp3fU6DrgxhdwYmjJLECi77Ub8tYl4rKwndnYvIgDG85pFwhONT9KroXnR36HvLfzGW+QTMTOYUhaMOYfQfcK3BiqK9CQ3BS0M+qZsdVYzsxpRuvN+LpLayxoCwy/91Qsb6rrc2xFtYsnvoIBDK6YB+/hM/zlRvHfs/U511wGsWMtc2Jd+L2WXvV4wuPOrZcYQQ8UZ6Ne9bVrfk1LYyw8fSbo3iRZeuGY0QsY3A6Ocq03SJ9n6ir49i75PnahWpV5rPm+xqAMK9CO6jT9k9YgK7te6CXtG3HP2OJ6guzineF637lMt8nCq0tXW83cSOIs7nBkiyQTHLxKYxQj2Pm7AA29CNO0GIKS91ws9vZ9y6rXtbdpJBdlfmgNxV1zp1Kn6avvKR2Gb3Qu/WXM8XJaji8+HrScuFZh3i57bOgx36PX3SeAJGnjMaGQ9nc3gflTahCGkXW0gGjeVb34vVuKa7fewVsI9wIRQ8s396zK+JYHyzxQi36WfLiJxxOSnN2ixUdnFRJFWHl/vNo0Q0hcr8/scNI/FV88zcV28fZkYh57yFEk25tzNYoGQkm8v7TQICrYRYOBHxT2Mq1YZo5lZnd7CkWcOzC4yvlO4T6sXZZrxAt3tcEC2gFoqF5k+qe3oztQImdPGrohlou6pABLr9h/eh9+k8inWaMPHvohLztlyzI2muVzYhoZqqmhJwrsdkHmijUKc1opmN7TThXZrm3YOKX6duunXrz/Ga6BmkMLaPXV+Z+Uum5Vdzqu5MJmE+KBUfF8YL3Bu1Jwnw0cbk1RVakw823tUxKCaZ+oi7OWvaaisX4pjNaB8GZtzCedHlbeNoCJzavXNv70tEuoTPLX6nNbEvir6KYWt2io9W5JdK9vCN33tZ5/H/TF65vGooD/GkXl69ybOn5ZG/D0TJH7pELcIQ067NfFtHkkiTRhth47HvPcC60pplqiP1ZGg+LcXAXSnqjex83g3WdKKDanEPWoN4A7sUmILR26q47XhD0TsebkriXU2odextt6BOjcdjS4DDWHrqGtc4dUIp0s3hgcctFMtILOxTprYsABSuB8glNyHcmxZWyKkWVJY+rx1TZd3Us3XXws5SEKNP2lPR0mcanhiHF0g5LpGPcID16rn8VbNr1CeiwL2t2LIGCfbozyqZ+wQj24AlNUzFdqy4hg4kMGZipQlvcmV4caPrD2uUc3TZq6xs+qY4+VSENXklbRgq2Ww9qlIu3DsbC8mVlu8kVbzHREM55jceAgeTjD7l18RS5NZf7iBOzwehaggUsrxAbV+Ng9HWkju+/FB07H2+/gvcWpCpVvhSrf6S210oZU0o0R9nyZLEqWnTTGDbcODg8IzEg4MClEOtuMKS/jyn07OWWTlHqqp3tcv6msVQfkILa7YUxoJxhxV9D/TK+E9rj7U6fwR7jP4Vk+yQZjvVUdIsNYD77Ya3ocq3nJx3Pp9pvm7/Ke4zDqb409cUUdwt1K98aGiyoSJPAeq4geUhwT7nbBoEQ2sA1B9bnNsQoIB57A0bCUmpCC0uV3S2o9dO7gUl+P54SKqmuKXRBvRzpwoQ/ryxLyGhv7vXv/eIO+uwnNE9ZXyJzChkQ+a4/FJ5Chz7Q4FC0zBJCKGESDrGrb/UBtKutap4X3fkIJvYCnl8ZQyM3sQLxtRjrvWN5OuKckwbIhebcsvzZ1mOwgEWdFPKvExwlYkTLZsAr3jevY6KSVZ8U+RD+8S3LhNn3PoB0nltLMbtHK6h2jv7QZ/FtsXIV3qJciiVvFwwZgdy1kcpvrbQJldeSiYiFAw6S3WqTaadnbmpd2tmug/m2WpcosMUj2HS4STZk/rY9DcU6ZtVOQLI3xNTCDy3UVZaMLbIEMGmQtE3LaEVgtBU+ZEb+cdHcZZnxkd31nKwNeBjE3iOH8/G3E/XmRoXPjrE+bFUmIutR65DT4e34RJ46LsM6ZkWvru6sN+22dyvncIZIUzN9lNeV73wIXv9m8KEkbHgbO8y8T7bm8zV8OGm2GFo02jM10mStHOLl3yVuSzHhVDsAGelYsnpjnas1TWrM8hO6uhBCYdrD6DPRpoOaY9wyW4+VxXchilA/+0t9PeLHtn8NflOSh6UdYVVJIVASuoOF2L3iE/bbdNKTqOynISfH9bD56sxrG3e9J3LpbmPfNJjdF4vNTOLln3jSNuA9s/yvHXMt12Ko40ccqsZ2M+mYrf7okJ8/5+up8YL+3THyCLHXJn/SBieLjxNfluLKpRYNzxDMNmWz5++8BrtiDf3J49PjBjNIqp2TKFzhS8xlC2MkHCUcGA+vtKSUqXhczK2f+NikSlyJKowY/DMe99uaF03Cqpb3pjdjbNrD/xb+utRJ9VB0zIPCi5Ty2X8kbJ1SBE9eLBGjmeNVmEb6oUgKUhKbn8jraPiyT6XG+Ny7B7dLFE1BOsru/q6wfoBQeu0UzUygYa6qg+ZHkvNm6s0fSHqTGNNFwnBQgwvcuDFaiz8tQ942lV3ffQQ3k6pKHuJflU9/V1KOiNcXvjCWeIfIXD3a9KepYA8Up+NnNCfAPiUlvkzfJnzIH8xRKjpcWvBVwaNQDFVtVxj7fXLg3Tu3ZlEr8/eGBXeCnvtFlUAYNHaqus98JOUlyd3Lv5C9nS8iThl0KXFLofxuDWgl2JZLm+sJvjhgJS+FfFfze1IvPIQxLoHuSwcD0NcNmC4cb4ZsxHbCHqTojFT77kTuzBjs8s5cQ9twtSKUwx0rNAgeWPMBVNXwv1PUKDibYMoytlS0CMFFUi/bdIg2es9xjjm5cNe1MhJLcuc69fjqLbQAuUt2S4HEGT8zNEsqS86D0jzyNoYsRg0SIkF3HCc5WVIXnjWn6a2KGXuKhesTh7WPFMN8TvUr91NoTF/dOQsAiEBeYqvQ4OGB5aJWdOH3p/ZnzgKtXAiPYCF+WpneHJXW4b8gtzaS5aNUFcEagK9JYatMwkdZ6xlBcGSeHMQ/oGpGPWiTh5AyjwsJ+0GXjfol81LiB8LNZtVSfK4xiQfuFVh8n+ZP01L1n5jcBqHH4prBCqbVCZ77i/V5h651ab565i4rVmB8odp+TWzQmvJkzMtZH663Snp5F6p8mTBxHtFkgDRvFghVEdRjGokV29UxVb6IxxnWHFXWba7brMl26D6xRXYI1xnUNQK4pxHblaIx/FFbbG/Msbz8K/b3PBAMb1IaUo3crEClgyRItIIotLbCOt9pywsCgW5Z+MeSjyabDmeYXhtIzYVU0PTPMt1h5Jshyr8l58LUms6QGaH1tpJnBY2oCtCMOE+bT6hRHWrKrmzUdTosT91nLvL7MfMPJzmjz9LB3WsN0BNb/KsLiqRBVbI8fmBIjRReKRhMdMBh3B5mimF8r+GBUi7hdIgxFj8hjXUhrfxorFs9lAXcNHxqrIN/djo4cRWAKV2vMmOwdKWjRfRnsEg55jVyTK2qzVy7DS09I93DE8hQY6Mi6d+gg5uYQ/uq4v8thbLeCMdsdgT50XgyRJwI981zmdgxdpvsVJb38P0bDLiRMgIUZSdrxQa/V6BsOdui+LtS8Z0563q+YnwMJWeM85iVOYAZ7sMMOoXLDvcHpxYXxwaxXkRDSGM3j2ZAk2mxmqZAH3Y+Hyqw8cRrxGYcPfW7kY4pELb0uGSRgsw3QP9TB48a/k/QHDfjqpofcxmH9KLoKK/+TzuEn2YUYUBBzSNywWxfD9DB/ofLc0gtK0hbNObk0akEN9n0KtBy/v+27H/Iu9wCUCxBH9SqBW7pyogEbFnY+KjQWB4uhPjqBd8k5ubkAmvVzen1CUKtuQFt4THTNv+isBtbbcdL6UMFjmsFR3DR9qKMkZG2yHfgvZEZj4iskhaO65TN3JZ3ZYsw2+ney3EvAnEVFL+11uVDwb52W51kLFuNTPWjbjRBNbZkd8kt12U9OdT2KCz6aekgfYw1QwC0ecbTBUiuf4D81ErmdVa777U8+0mUn7qFgQIVowq5FSR9li9/Fv9Fim/OgNpDutphYYCxl8GA5WpXDy7toIvyaTcV3Tk2hZ8VTvVLc0ZeQbCVxjAU7TflpfZldTNYavs9IQNmrdCRklwHcHbwKH7bCE5BtqjjflprsjzHIQSY3WnQWynQ4/gRKsRbCYCeGoI25a3c25UecFSU8U7DD5LDQPT3gmDSZLZ0NCA9pk4QU70KH3xfdo94xDvijbjEG6gk62ArUkJCQtkPtrEHfgRN6vKGDJkItTlA799c3+8abs5VWuRvX8tnWjpCBtvf7rnW55vzP9zdbBLDbPi1r1xHZZab8C/dXu/vRAhKK5Jp0EJMPzZcaVOFn3w9DSUHgrC9798YNcPKU9HdOFM0tCqUxcQzy+WwsZri7nwBBfXRtu+GyB6tLr3RM3sGH9mYxWWMC7kZX5YvJ5oUKpeDivbAnUN9YiLx2GGJEnq7OwZ6Ce4CBIYfIkOcHhkfDYiq98Prsy4fiZcMBiDIRCRbHjJophxQ3cHn0ipdzGDtsUFMeUosrfRfr6c/U70qg7OdJ7rBWe6WrkVHImvyjnt/yEfG4yMAM1RPkxGdhKPSRGlZVEQ7xL9voHG2L+jH6h9VF8KTs5VFoHV3E4sJzNqpm9gO1m3F0D9PpLUmrK4vCq1kzfOuqsbX4bcsiT84xTmHIlbCvCHNjR4WO+XUy3aFZL2JM0f99a/ngu58gEynulI4F0Df5UsuaUas/lL8O00Cz5EHJZCvpyn3gvKgre5sQKX9RbGL7jjZFfQ7/gv/N2Z+SY2K6JcY5f0EZy3rHY5Dt8NVM6pKKfxOKNvfKcHwYVgUSJJ0NpTTj7Lbzrf5N+bxwfjYNQq2ydiv96t/+c0xmcFbpQ2R2NFICT2ko5z52ZXOI0Adf3KzCjnaz4XJjjvhP4ndzj+r9OmGXu8EhDDc4q3p+fbC9VCKchNpw87S6E5F1znQ/HuSJvRG9UESqLFyr4\n" +
            "XNR5k+sIYw3IIwEtfMDH8eWq6tpDnSbqxOxa6JgCpchbLAnyLPt1gbgFbPqf/4hmNckqazrcF++92SNUmFbwWCuiHqf5YnMK7hN8oSDLxn4LgdM0PbW6WKXrlP3GLAV/HEguKFrtmBB8j/xnk0QZxs+ABYfZNCeC9r0T7A+8J9G1YAW7pwPl7Hm+a6lc5gHqb/bMmjw2OSLTahSKZWw4saNxQXw9RvyXfreTz9fX/SzdCue1iUmPX7E/9WRASeeHR1G2y21ILpOuqinZ0raC8jqdW2RXyqMnhjBDNsh8SR2KQaPhAE4bO/CZfufxTUHQPNVtYOn6lPkU5t6LzcOK0GFCPBG9CzrWWUXEKOwaE2ef/jeGVdye7cmz5/0+ZxS3xfbX7O9FPSjqfnNREO+ELLy69CyrsWkkDbPxKolu2Lkon2Xcy7zT0Uzvo3DeGIDfc1aACm2o0VpFpQRhATVvPGSkz+hKMBFnxkx6Egt8+ThbgFHb9wMrdVqs52xJqO6Bck9fLLCLWSfApeDbsYLS4gdNPfG4TC4zOWJZChUIq1zYXUwWJsO2JDwIU/1giAAsJWjOLjLtpeV3m7Dp+/ND5BvTGcXrzdSi6RggDTikPChZNoeI9vH1C9H2I72Z8ZxZjUSjdeSihoa/kFh7+NW3Mz84R3Q9z6RBpCzRz4YNEci7VF9TE83m/WlVcgHywbx8sVrFo8nMm07YoySH9OkwDUYcO+UU03oMK+mZb65ylxffaCeZSG7yJhHk39j4FxQPJn67yLVgtv+lqYVhk3Gj/mc5S9LyA53CIL7H7YgfwrpIpAaTRW6kNumBv1Vfk+L8NFdB0UTxbHpLINnaUgRrWueVs4zcuhK8a2XSXKmAXlbQ0juirJEdyUKzb/N9QqsRtwAZwybOhTI2s4B0L7mLNxT+Rz/K0A+yryeeMLtblH66Lea/L01UrOOlUKbXZX1Sk8fb79YAO0h8cTRtljh/o43hgTBSDmaObyLpYxe2dOtY4LPeGYyKkPn8qyFN2t2GLfxMyTH3a/LW4k2xaq7WaZfEj1k+fsBcX9iKPEELP4EHdNpq1ZtBbr11+Uxk/ffAUkxfmGxhnZBEWWyx35I7e8M1T0PfjStd6jqp6Lkf/35jkIx7OvNBhdxWcgmQBSEsc70cRNCUfkx7Gt663LMGZPV561Y650keEZ8iENpijBUDjLFTiK7E8EaUQnzANclrRBkJi0o9LLn+xGblx5RIt2Y/qE5+liZ0PUHVHY3w3gYhtysl2nLy+s/xjNO0yS/ZukU8px9SsH6vp6zTbJV9R4rwBV13GNwWS85ZE9G7JzQ8vQm3FJyDoLuzeLsE71DCoIsp+z5BzsAE+0epBlqeyzYeC2dxkAMOAROjRBiO+4+GVZV33QTb3F1+W0IWUxXigVKfkWI+uM0EVfvi5lPLu2PsSV+eVYcu1efM2a+Z/HqLmQICcpBnCwMeKV9Gv/PvT54lsouZPao2N8n7X1GOnH7pWZIoZgG+/1E6vaYgYhuP+Tk7KVeK6u5vti18wJbuy6PgseVULn1OM2MOrl11JLlVMWWp+hHxHB4+aingSEhjztybi/yhqwFAH0Kxlwdl1l+558/2KK4deutsXLBrAD99KNeFVPyWDVVq8ZGSdnPCjwDnPl6bbgs7s7XnlxgpUeQRhgSzRxgHZJnpesEttHnChXCKD7vQoWR0YrNoL3vakDuF1t6bXSsj58tP6N3Q5ladt4PDo5G1rGm9TZjYC0VuJ6QFCFqYNqM9+WXaUle9Ej8B7ylRi0pahATjxJBDhoK0mYRi006Rbh84OtzKkSkj/GmtlK1A9z4yG2mVhOiAPOlrJdNueWTGFbk7tbkCHyFdgWqbMpCGukAqmFZKxV1gVqcMhUhBNuZd0LHlwXpKlbDeKV+M/oPcmNLc54wNPKe/efZxUhx6Mr1R6aUwtaLJTVIBSYzegBLOw02C+lBNxsR7phk5GbKzrOAd19WTP5MPnrh6bsJNeM6n/g6bByoCTUFyGmudlAvFEadaG8a0Zec0x9i9Av3qjUwR7JZxDSw7Yec4p1KhxcIOlUHtpAxSvYKhhxm98Ylx9wLMXyZ0GaTxyIUx+MQLert3XhOD4lp9ou8xP3oTf9iHOAYNu7FuD2p9P/rBdG5vMlDjINqiosSCt32DPEVbRut1ruOV60TW6OBhtwHOBxWHLUA5gNMFogdWUzyD8w0maz0bqKuUVTUOMlNCM879ZsUhzTWu1hv++UbFYW7UsATcae1FUl3WU+wzq8jDvp7UCVUO2FO0M6shpbYx6qJea8mQbI46dtaGYQaTdBMqxMvMnYDlhDpQqUg/nlOvHgK2ygCsE8vywmcRq3nlHSHqvFO4GggNuR/G5pHAZWDRrFiMnSIB24Yg1fwhR+9P2Q6i26qrowWejGQybCr75tZ6ibgobecnvt9EH5fwXFJb8yX0Hfj0VkEn+bIFZTNoWcmVql0oCdYGbTGBfBTLot1BiA25Jd0n7nhF97CjC5iVkjtvoEn49AVmXLeTFS31IHOMZDJVCLkOysW+9YN0+DpLXLm7n+ksemwB11d3NUw3vp2qrcbWkAWO5852GxExpGsk3a4E6mmHEDLxDNaHpJBwyBU5cN2VSmc/xXDm5pND9Fy67b3rtZyjn9l5oVPwBezFrMPasH44WWAN8lpGAu5PodOu1ebNax9b3GVyzKVQgeAqv6Nw2xhjVVSY4mR/eRYNKZIbQvi6ipTDGNL2bM5Q2Qrm7n0t8k78U1+DIc1Q/XSRe27Uz0aG6MaeVYG/zTL2sXI9aSJciUUbW0i6+V1C49xS6ioEGBme2f1rkvRooozKzdVlTJCIiUZyRT3n5Gk853I1ztmeJqXwojyRSAvpfABfaiEdVcKXSDwLqSQLEUUT72viVBJ3TyvEVzcJB2WFFeMFR38nad5d5dr4ETsf6rqEJhQJwA5YWSj6bveVxfQsCrpFp+1Zfp6gxvtt3zPmdT5Sy5Ms+A6tRTG/iKpFIHrRMJw3eblvZvhaoDwUB4U2Q7Sdsnh0iquz/iTni6eecX+sSUYNPfhHRthPmibCFv9lRO7ROfg6hXocfjKdb5OCJjq3Uy1JteXA4rGa0Dhp3Ms7aSB5K5KGF1s5RuYRBmug3hyg5dt7ZzG4Y9un6gauztVJTA864uPA+1RTWBy+m9CQpi+JRxGs78rX8NgDrINircNq1okW7qRTabmpGaflQq2IryiSvb+IkA6h/ThHU6WyK7vIYFm+OGu5Cktq4CK5Nnyzuhe6wdaox3IUSHEbADa8yM3IGlIfiwDjDo2MDskheuiUbADQcfOuS9rWm5oGxTxQtqMo3+8KnT/h4k30iUFDPjLG7JXUsp3FCSSejXuhsR7xr4m2fuLiVhvq2vgk3sTKNxLW6nAO+OE63xd0ZxScg7qgVYkGZSDilGX8w91VRQJ23Gwk3R9kpXRY9xghJS+1PMjw1zQgW03duzfrLNHbeaN9DsODWzyzCcTImcO8mkAqdvmcI+44CiHat8Fa9ABZiHYpmgxiWA9/dUb1wKh/oK0ukZfTrngXn/PZeTBbNDZqhHzn6kMolJ9jSjZZxAxlV0Ho0w9DLhxqrJPtH5g4SHxUjynDosjvUViE8vn9xw3XPT70jt/HmjSjFhj4D2y0XqWff1+tPOUdQBOfvrROVOQzRpJXzbsu35oy10o4mnNc3EBLEbBsbGwd9VzEtjY5mI6ev3XKcB5ULu0fKK3tidYW3JzovvpTXhV5u49pHK/fGEQRD0sHYgYZGSSMk5teZ5iIs1E+ENQi09ehR5fXQ+7bAjJKI1l8VyoVRwBqS8uDfIINy9wiD4NynSpLQ4NgIfPA30Mx8yvxaxKvrr4y1pBzCs94KSMzqajKMUk1bNnW+p874RXqtjG1Rfdxc0mxR5nAMHcdiGtNvDcCI570zCnI1VZS/Lq57j8PXLBZFd405S6//mYQT1Uix1qFCOiewUnei+8yiWfLaNzq+S/ExzXlJVVSMvEN5XzBaqdAtos9WRv61F6lQ5kUcYNtxkQQKnxwAFk0YG2FSHuLLdJENkdLb1i03MJP3q30dG3DrefddYbssFIyN6yMvoUnlEwlOLGU8iWC1DSNtFLE5YVKo0Mjv6Vu9zjzE6XyiJJm1+RclCFw83yIF8BqwruwNdglLy5zRFWUg3uP7UvrphxmmDkuG6E86BVziL8yIyILny2jAw7G1DfwnOzsQ0qQoQSLo3vn8+2GgVz98bCY6Y3fOH3AFi0MizvuIkjiJwxHxNsUGmcrhBOnvwi+klHL8A0EXbj7/xoASgLnECNFbeW+Mbp2GseiMzrSux37bkOiRqbUg+fwwneBn73nGEytAz4WZzP/UFCn59xs2r4t7/8J+tRw9fiFN3cWy+SHbr9XuPJHsca0Oano+Db95SD4H47OWj+JC7wHTBLsvxpEPaPwXz7In8ifXK82L07+7grYIXcYHdCbky2IcCjNpH2vur80S3y8khikeqJ0j+Uh9noSTfC5jmNXFyXs7fK2q+e5HuoQYtMwbIXznec6X7cK8ggIRp3fi0zCz58g1+B70M9PjwpxGnzQvogWUJdnFWGizEn7Uajc8q7DvqHsHsPzpxGrgUlEjF1rp2sc5bJxzeNZPiiZ00FqCNRtMzdNq1j+j2gIk1T/1ruFn1T2JuSYGRHoVaEmzAMVOMJflAJ/M+WWr/iZ2vJKMd5REsiG3jnXU/BIBQogHNxsIdqYHqix7QgPt7RS5OIx9NmNBNozczyarCzo2iPYd2h2C48mNb6hmTlDCK2l+dlnSFIiKOpDvU5qG8qnnQPCyzEm5byXG9rpMZebCDbytRor9FzK34FY5Fq1WUZaRRd6vt0hc0KYF82yDLXSL/RU2bn5nWfJogah2+FgTsZEpImsTiNmyLa/d/WyTGlvfuflJGpAyBJk8eKxRifYI/Br1rUclazq/lPI/QWqN/EFSFsfiJvJO+ZYbNXuqhfon0EVRw5oew4TKvJEWK9MO1hNjDM7j77Hc8ZyFncD/QhoLzPrdglCFu5N4DfMC48r9Ctd+K1nNFDggTtGBiAtarBEK0hjWFtJ6q+xHDrCn74By1ag2Mi3oy5fHMRuSsFP5KBhSmNb2aKUktJb8YaqZ3Lfi1OtvWifUzE4/GI6JkqeJxh6Tzezo7wop8Atwi5BvsUlhVX0MssiIESNMD6fKSkOhEDiprqyJFVSyh6/I1K80SHA3ThbCE2hZqzFc/nRZ0Aq7tH1scGqpNZ42HoprXyKM1esLt75eyeExnCfaFuOgXQ0//3IB4SKZwB2/XZX5IlO+13ayNpIYXWIEe560sfvMwvDI2qZnzj25idj6842q4VA3MbR93M7W7USurVsQBOErNzKwdgapfSf5dnc3xZ4moi44O38/1A4qN9ZWsFqrZj5UNbM0djpWylEVwkdUkmQM1968s6GDnXaWzCkVM9QFiAa4RIxJdAGCsCfFKz602voyCGS4GVjRbTTEDzt+O7xdO8Koq5J4MlakX/hzWSmT/12RDphamrV4q79oYu5uh8nASZ7Psjwy3r0tsaw0tle5UEqnerDYVaBQwk+dfouA+M1kSotLhbID716nKVgdm8JO5i+k4xpx3FVukpNzGkbFd+lxhSH1mt+UmPI8l3G4oiAYPLPl50MYn7I/Azt3tu1gONJAYiVlLV8nFVkJGBRmbMmoJpdU5pg/9uzSymLk8u7vM0jfWDcbTclEAdWMm4tmWEpZRzy+/xGEEpoLAnoUKVOhO3+6Kg3hx3DCth38Sh+zGO1QHg9NW8Ve6a1yCTsKicAUwEpseYUU2NRBl3s2DWZAguoln2+n4aCzL07Xzh7ZyNB2CacbpaURhVlDskfjArKIrnz/5QdaLsnysm6wyvhNCXC44jDhB7Dj6APKO57g2rlDDpnVjVVki/DYeEuTU6YGMJjIwIxbrUeuNcEaOJiO/3KjdgeFPwwVuClgSEKdUuoxrtMQ3b6ID6dZ4qKl0Q1WMacrwpd3Q53nyfz3g+dyrLDXlpYBJ/th9sHLE7Jqe0edHfni0WJz/zAwf6gPNcnvsKDG+Upxwllm/ww1Eu/B9Fm+v0GEyS38ZVabZd4iEF6KCKQ7B2zk5h2nB6/NEdMYu2b3BqLIEI4mX1XiZ4pnkvC3hrv19T5iprgoGzf8p9pPHuK7GE8t1F5t09qkrxuF7ZlV7xxh1waajHCm/xxSItt1yxBVEN/s4gfvSavVz3RImJ4xB/i9NEiMFG39TVfc4hKsf+VQ4yYJfArrAzd5egmPbE4RFmeH4SBw52ZObIyPJX/MK1V3iwW3vEJAemCKEgB20INE3Oqwikf9zIGXS0GbZeUUA3MHAipWpFMtuuKEbxZJ6+KwdrKfhZG+zKq5AAFwLtCbFhfCfBYfwsAgH9Z4MB/scC977ANMz7cH8MBEGQASBZGEAsTe1OQHADxtbbBDgp2+zeTHAXk+sp4h/wtUfgPt/xvFYGTkfl/iOcIEBOwpQP037OIzkZgSyO7f3VdHV5e2ytrXUwa+pdNDmv6jxL+X1l8VygAAyQvoWqgwQb8wAV83afD33KAf1cUAP9aAf67oiB2TCsbC8Jg8x9GP4zDq0cY/3j0j9g/xQQR27EjMVRrsACYV2K+s5GNE8j1P27/I6TgZGsMAgeXJ5KiJqxH/GepvzR3nv8FWbZiew==</data>\n" +
            "  <info origin=\"10.237.204.22\"/>\n" +
            "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
    private String input3="<SOAP-ENV:Envelope SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
            "<SOAP-ENV:Body>\n" +
            "  <request version=\"3.7.0\" format=\"de_CH\" language=\"de_CH\" service=\"com.bsiag.scout.shared.services.common.file.IRemoteFileService\" operation=\"getRemoteFile\"/>\n" +
            "  <data>eAGVUs1u00AQnpom/VP/JS48AQc2QQgoqsSBQpVULlRNQag5mK09cbbyz3Z3ErmXPgNXJCQeAMSLILjyEnCHE8zaTtoeOLCHtedv55tvvk8/oWENPArzVJxYJWNhw3xEwg6lwUhokxfnYkikRYevA2d2ZBYlaA7xbISWoD4ezPehmQbSxOzc7Puncixbiczi1ouTUwxp24f5NEjyUCZIsFHFR6SSll/6OL6YBimSfCpJTjOuv7CUBlliq4IzuADPB3blGo0klWfXq3pkVBZv92EtDbQ0kt9Gc3SuHeqNqwB3Emkt97+VBhbNWIXYzTh1IEMsQ8+51HWb9WEhDcZoLPcqHYUemcmwwkEV9bBvv73+sGZvJx5AoZmiGSb5wT9IrntawfE0z8RAJSgOMc0Jd/m3JnjGg6UurKZByHNiRj5mMQ33oMEeEx47crFQluwerDDP0tJ+HqmBwqgLmyUB1CNp6CC3ynHlwzJX8pot0nTAkiuGoQ1ai1G1Ca//pEquGjsGaza4d6RMbUy362xeDIshmxBX6D9XzkQz4HwEjZdHu3e2tCaYLWzSYqbWnHiEE4eodn3xa/3N+/bvHzeg2YX5obTDMI/QhzkWa0bmvIawiAVT47ZjJ6DcUkYyniBu2tAoTXV0biyNklllVhgJvJ0Oq5q/UVm07nLXC4IV3H/WC17dFe37gnE696oub4LlGOlyY6yJWl+VJip9fbz5/fOXr8fvppIYu+IFgsf/o4vuZZtepVUm8J54KNp/ARLEQ6c=</data>\n" +
            "  <info origin=\"172.21.12.205\"/>\n" +
            "</SOAP-ENV:Body></SOAP-ENV:Envelope>";

    @Test
    public void decoderequest()
    {
        NeoLoadMessageEncoder messageEncoder =new NeoLoadMessageEncoder();
        messageEncoder.initialize(CustomDecoder.class.getClassLoader());
        try {
            NeoloadSoapRequest request = messageEncoder.getReques(input3.getBytes());
            if (request != null) {
                String output = new XStream().toXML(request);
                System.out.println(output);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void decoderesponse()
    {
        NeoLoadMessageEncoder messageEncoder =new NeoLoadMessageEncoder();
        messageEncoder.initialize(CustomDecoder.class.getClassLoader());
        try {
            NeoLoadSoapResponse request = messageEncoder.getResponse(input4.getBytes());
            if (request != null) {
                String output = new XStream().toXML(request);
                System.out.println(output);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void decoder()
    {

        //Class<? extends IServerSession> sessionClass = ThreadContext.get(IServerSession.class).getClass();

        NeoLoadMessageEncoder messageEncoder =new NeoLoadMessageEncoder();
        messageEncoder.initialize(CustomDecoder.class.getClassLoader());

        try {
            Object result=messageEncoder.readMessage(new ByteArrayInputStream(input3.getBytes()));

            if(result instanceof HttpProxyHandlerResponse)
            {
                HttpProxyHandlerResponse response=(HttpProxyHandlerResponse)result;

                String output =new XStream().toXML(response);
                System.out.println(output);
            }
            else
            {
                if(result instanceof HttpProxyHandlerRequest)
                {
                    HttpProxyHandlerRequest request=(HttpProxyHandlerRequest)result;
                    String stream=new XStream().toXML(request);

                    System.out.println(stream);




                }
            }

                    } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
