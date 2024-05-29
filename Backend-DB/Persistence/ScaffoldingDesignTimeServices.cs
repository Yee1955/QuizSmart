using HandlebarsDotNet;
using Microsoft.EntityFrameworkCore.Design;
using Microsoft.Extensions.DependencyInjection;


namespace robot_controller_api.Persistence;
public class ScaffoldingDesignTimeServices : IDesignTimeServices
{
    public void ConfigureDesignTimeServices(IServiceCollection services)
    {
        services.AddHandlebarsScaffolding();
        
        // Register Handlebars helper
        Action<EncodedTextWriter, Context, Arguments> constructorAction = PascalToCamelHelper;
        services.AddHandlebarsHelpers(
        (helperName: "pascal-to-camel-helper", helperFunction: constructorAction)
        );

    }

    /// <summary>
    /// Convert string to PascalCase, camelCase
    /// </summary>
    /// <param name="writer"></param>
    /// <param name="context"></param>
    /// <param name="arguments">string itself, expected string output type</param>
    void PascalToCamelHelper(EncodedTextWriter writer, Context context, Arguments arguments)
    {
        var InputString = arguments[0]?.ToString();
        if (InputString == null) writer.Write("");
        else writer.Write(char.ToLowerInvariant(InputString[0]) + InputString.Substring(1));
    }
}