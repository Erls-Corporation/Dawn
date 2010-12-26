package dawn.core.services.pricer.blackscholes;

//http://www.cs.princeton.edu/introcs/22library/Gaussian.java.html
public class Normal {
    private static double pdf(double x) {
        return Math.exp(-x * x / 2) / Math.sqrt(2 * Math.PI);
    }

    public static double cdf(double z) {
        if (z < -15.0) {
            return 0.0;
        } else if (z > 15.0) {
            return 1.0;
        }

        double sum = 0.0, term = z;
        for (int i = 3; sum + term != sum; i += 2) {
            sum = sum + term;
            term = term * z * z / i;
        }

        return 0.5 + sum * pdf(z);
    }

    public static double dcdf(double z) {
        double epislon = 0.00001;
        return (cdf(z - epislon) - cdf(z + epislon))
                / ((z - epislon) - (z + epislon));
    }
}
