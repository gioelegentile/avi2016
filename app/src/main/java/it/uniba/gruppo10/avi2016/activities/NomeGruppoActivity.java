package it.uniba.gruppo10.avi2016.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Random;

import it.uniba.gruppo10.avi2016.R;

public class NomeGruppoActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    EditText nome;
    ImageView imageView;
    Random rdm = new Random();
    int cod;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_nome_gruppo);
        cod = rdm.nextInt(999999);
        nome = (EditText) findViewById(R.id.editTextNome);
        imageView = (ImageView) findViewById(R.id.imageViewGruppo);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("outputX", 200);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("return-data", true);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nome_gruppo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.avanti:
                avanti();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_PICTURE) {
            bitmap = data.getExtras().getParcelable("data");
            imageView.setImageBitmap(bitmap);
        } else if (resultCode == RESULT_OK) {
            finish();
        }
    }
    private void avanti(){
        if (nome.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Inserisci un nome", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent crea = new Intent(getApplicationContext(), SceltaContattiActivity.class);
        crea.putExtra("nome", nome.getText().toString() + "-" + cod + "");
        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
            crea.putExtra("foto", imageEncoded);
        } else {
            crea.putExtra("foto", "iVBORw0KGgoAAAANSUhEUgAAAMAAAADACAYAAABS3GwHAAAABmJLR0QAAAAAAAD5Q7t/AAAACXBIWXMAAAsTAAALEwEAmpwYAAAAB3RJTUUH3wYMCRwS8DVZ2AAAIABJREFUeNrtfWeYXdV57rt2PXXOmd6bNBqNNEINJGEEQjQbY65JrguYOCZ2nHJt3yROYudJ4hDjJNdJ/CSObeIWYszjBIgx2DQjQIBQQwWEhNpoJI00fc7U08tu6/7YZ8qeppnROWcfpPX+AGYzM2fPWu+71re+9RUSCAQoGBiuUnBsCBiYABgYmAAYGJgAGBiYABgYmAAYGJgAGBiYABgYmAAYGJgAGBiYABgYmAAYGJgAGBiYABgYmAAYGJgAGBiYABgYmAAYGJgAGBiYABgYmAAYGJgAGBiYABgYmAAYGJgAGBjyAQIbguxANyiCcQV9YzF0DUfRNRxG93AEA2NhDIcTiKU0pFQdACCLPNyygJICJyoKC1BX4kVdqfnvKr8LfpcEjiNsULMAwkojZgaqZqBzOIwjHYN4/UQX3usczujvX1dfilvX1GDDsnLUlxRAFNjmzQRgM1KqhuNdI3hy/1nsb+vJ6WffuLoWn7y+CdfUFUMW2UbOBJAjUEpxvCeI/9rThr0nO/PinbZf04BP39SC1dV+NkFMAFkycXQDO4/34N9eOIxIQs3Ld/Q6JPzpPZtwW2s1BJ6ZSEwAGYCmG9h5vAvfeOrA++q9H/rk9di+pg4iEwITwFJx4OwA/vSnu97Xf8N3Prsdm5oq2GQyASwcF4djePDJvTjXP3ZF/D0rqgrxD5+6CTVFLja5TABzQzcofvbmKfx45/Er8u/7/dvX4DPbW8ERdqfwvhAApRSaYRJTp+bXBgA6xxsTAhAAHCHgCMBzBAJnPiOEgFIKMsfkDwTj+PKju9A5HL6iJ3xZmQ/f/ux2lBY45xzz8bGiwMT4GxQw0s/mG38uPdb81PHPY8HlnQCmTkBMpTg5mETnqIL+iIbhuI5gQkNE0aFo6dcm5oSIHIHHwaNA4lHi5lHhFVHvF9Fa7oBbJBAIQNMTM31Cdp7sx4OPv3lVrXz/9JntuGllxYyx1ylAKIVGYY5/IInOoIqBiIrhmI6woiOa1KEaFIQASE+DJBB4JR5+p4ASF49Kr4D6IgmtZeb4X2oBumoFQCmQ1AxoBkUkZeBAdwIdYynEUgZ0SjEc06HqFAnNgKJRKDqFll6RxgUAaq4+Akcg8AQyT+AUOUg8gSwQbKp24vpaF5YXyzM+/+FXT+HxXe9dldv/525fh8/fsmrG8/MjKRzojuNwbwKp9JgnVAMpnUJLjz+lk2MPAFx6/CWeQBIInAIHkScocfPgCYFb5rCsUMb1tU54ZQ4CR+AQONitB9uuEFWdYiSuoTesYTCmIZzUMRrX8e5AEqNxDeGUAUopkhoFASBwpmnDE2KaOuMDl54AgwKaRmGoBvTxbRtAuUfA+konJME60ppu4GtP7sPuU71Xrf37k53HcHEwhK9/fLPl3kASCFQD6AmpCEQ1cDDNGT49B+OmJqYsnZphzmlUMWCkTVcKwCGYq3+BzKF9WEEgoqLIxaPAwaPMLaC6QECxS4DI26OEnO8AKc1ASqPoDqk40pfA8UASg7G0aZM0YAAQ+bTJcpmfVejk8am1fmxrdMPn4CffQdXxhUdew+meUXYKBLCmrhTf+9x2yOLkGIWSOnZfiOGJ94IYS+iXRzIAGjUFwgHwOjj4nQLK3DyuKXdgY5UTtT4RskAg5zjGKesCmGr3UUrxi5NhdI6m0BvR0BlUkFAnP36qVXO5KPMIuHulF3c2e+GVreT/7e++hJ7RKGP+FDSU+/Ho/7ndElcUSenY0R7BC2ciGIxql8+FWebXKRLU+yVUewXUF8n4eGuBhS/ZPjPkZAcYS+g41p/Akb4E2oZSCCZ1JFQDOgU0g4LA9NpkCiUuHrct9+CjqwpQ5JqcUEXT8envvoSeEUb+uUTw0y/cAUmYXDBG4xqeOx3Ga+ejGI7rGfssgwIUFAJnOiacIge/g0dLqYyNVU6sq3Si0Mm//3eAsyMpvN2bwMHuOE4GEhB5DpSaq0E2tC0LBHc1myt/nV+y2PxffOQ1HO8aYUyfB+sby/G9z20HP2VF6goq2NEewa/bI0hpmafLOBcIMWOuWsud2FLrwnXVTqyYxXGRSWTN4KIU6I9oeOzIKP77aBCnAkmIHDfhQ84G+UWeYEOlE7c3WckPAN946gAj/wJw9EIA//DMYcuzOr+E25u82FDpzMphlUzhjMhxOBVI4r+PBvHYkVH0R7Q57x0yAf4rX/nK1zP9S8NJHW9eiOGRt0dxejAFVaeWFSVbqPAI+IMtxVheJFlsx4dfehfPvt3B2L1AnBsYgyBJWF9fPPHM5+BQVSDiaF8CEcXI7qpMCDSdYjCq43ggCYEjKHMLWTkgZ+Q30ikSHYioeK4tjGdOhdAVVKFR5OTqvdTN48Mrvaj3iZbP29U2iMf3nmGsXiR+tOMI3jo3ZCFlvU/Eh1d6UerOvm3OEQKNAl1BFc+cCuG5tjAGIuqsnLNdAIQQGAZFx6hpK+48F0VvWENMNcBN9dlnCSIHrKt0YnujB/IUf38gnMRf/ex1xuYl4s8efQ3BWMpyvtre6MG6SifELHsrCTEv12Kqgd6whp3notjRHkHHqALDyJx3KGN/xrlRBc+eDuHpU2EEohpUnULK0eXG+iontjW4UeIWJgbGoBR/+fhexuLLxF89vm9itSWEoMQtYFuDG+urnDn5fIknUHWKQFTD06fCePZ0COdGFftNoKlb0Du9CTxxLIhXz0Wh6jS9hSFnA7StwY1NNdZQ35/vb0db9zBj8OUeii8O4ukDZy3PNtW4sK3BnbMFbpxLqk7x6rkonjgWxDu9iYyYQ9xSyT++0p4IJLHjbARH+hJZPa3PNTA3NrhnuMq6xxL47q/fZezNEP71hSMIhJOWZyuKZdzY4Eauq7VQChzpS2DH2QhOBJITO9NSRbAkAYyTvzvtHz7Sm4CSXvlzOR4lbgG3LnOjwmsNafrmM4cYazOM6WNa4TXHvsSdu3CycW4pOsWR3gR2tEfQHVQsnMyZCdQfUbH7Ygz7O+NIarkPKPVIHNaUO9Ba7rC4x3afCeBoRz9jbIZx6GwfTveOTjkQc2gtd2BNuQMeKfd5x0mNYn9nHLsvxtAfWXqRgiW9eVQxcKQviR1no4irZnhsrlHhFXBzoxv8FOVTSvH/ntrH2JolfOVney2mBk8Ibm6cuQPnAppBEVcpdpyN4khfEtEl3k0sSQAnA0nsvhDFUEwDBc15TLfAmTbo2gqH5WZy58k+hBMKY2qWMBqJ4+DZgYmvRZ5gbYUDK4pl5LpQHSFmLNFQTMPuC1GcDCSzJ4Cpqh+MatjbGUV3SEVSMyZjw3OIhkIJq8tkOKaMum5QfOuXBxlLs4yvP3UAxhQ+OAQOq8tkNBRKuRVAWgRJzUB3SMXezqglYnWhh2JuYWozKR5J6XjhTBgHuhPoC6twCxzsSGNoLXOgpdRhebb3dC+iSbb6ZxvheAoHp9wQA0BLqQOtZY6cvwsB4BY49IVVHOhO4IUzYURS+qIOxQveuBKqgROBFF5pjyKY0OEQORg2TIDEE7SWO1DjEy3Pf/jqccbOHOGHL1tdzDU+M/dasiGrywDgEDkEEzpeaY/iRCCFhLpwZl5SAONbSW9YxQttYYQVHYQQW1Z+gQNWl8ko81gPXSf7I+gcCjFm5ghn+8fQMRyzPCvzCFhdlvuzwKQ5RBBWdLzQFkZvWF2wGXTJ1yWEQDco2odTeG8gCUoBm9I3wROCDVVOFE1LlHj20FnGyhzjqf3WAMMiJ48NVU6LVy633DAvyd4bSKJ9OAV9gfFCC9LrycGkSX6bB90hEKwuc1gyhXSD4oVD7YyROcazB9uhG5OmRqGTx+oyBxyCvWUeKEwRnBxcmFdoQQJ4byCJI30J6IZ9EuAIUFkgosYnQphy/77vHEtysQvnA+Ep5ilBjU9EZYEIO5vZ6AbFkb4E3hvIgAB0g+LCqILOMQUxldpy6B1HgcyhqViCY5r99cIhFutvmxl00Jpk5OAJmoolFMj2VaQ2YBb06hxTcGFUueSiPe+bGhS4MKZgOK5D5AA7Nzefg0dzsWxZXQyDYu/pbsZEm/Di4XbLnQBHgOZi2VKCxo4DscgBw3EdF8YUXMpomVcAZvEqHaGEjlS6QJV9OwCPZUWSNVl7NMZYaDMCwfjkQZQjWFYkoUC2VwApjSKU0DES1yfC8xctAE2n6A4pONyTwGjSrLZmJzwyh4ZCqwAOnhtkDLQZRy8OWwTQUCjBI9vblEM3gNGkgcM9CXSHFGjziGDON02ls3C6Q4pZ0cvG5Z8jQKlbmJFY/1Z7H2OgzXjzlLU5IM8RlLoF2/miphfwQFRDaikCUHSKYNJAVDEL19pZxLTEJaDUZd1WKaU4dKaHMdBm7D7VPePCqdTFo8RlX+dKQsxo0ahiIJg0JnJVFiWAhGZgLGGAUsCw+QKgyMWjcNqAjkaTjH15gkjSWjax0CWgyMXb+k4GNS/GxhIGEpqxeAEMx3R0BhWzKQK1VwF+Bz+jTF7/GDsA5wsGQgmrAJw8/A57BUAphUEpOoMKhmP64gRAKUUoqWMwpoECttpzgFmUabpvuZcVt80bdE2LwyqQOfgc9h6EuXR18cGYhlBSn3MRn/Mto4qB/oi5tXE2K8Aj8fBOS7u7MBRhzMtTAXglDh7J3h1gnLP9EW3ebLE5dgAz/DmYrgtvd1Mbt0TgmiaAblbhOW8wfTd2SRzckr2sGf/0YMKsRD6XFc/NdYw2m6EZeSEAh8DBOa0U2QA7A+QNpp/HnCJnydazUwCUGmYQ5xxuzFnfsjesoiekIqVR0DxooScLxBIABwCj0QRjXp5gJGL1yAkcsZSotO8gbN4K94TUiRyBBQmgP2L26DIokA8tJGcryR1JpBjz8gRT64fON2c5FwBMd2g4NXmeXZAAwgkd4aQOnrc0ArQNsyVZJBWdMS9PEFfUBc1ZrslPAPC8Wa4/PEefs1kFkNQNpHQjbzqKz+aEMihrcJ8vmC3kmMuTdsAcIUjpBpJzBLPNKgDDoKCG/YffqWqe7Q9jyA/M1vwkX5YnAoAaJqcXLIBMdmvMBGZ7eYfNfmaGSTglYUFzZpcA5hMkt9iV1w7MVuXC65QZ8/IEhW55QXOWL9bDJQXAc8T229+pUPSZ8UhFHidjXp6g2GudC0rpvBGYOT8HcGTOHnWzCsAlcXBJnK1J8JZDuWYgMa0CdbnfzZiXJ6icNhcJjSKp5ccWoBt0gs8LFkCxk4fMk7y5B4grBuLT4jnqSjyMeXmC6mLPJefLLvPHoIDMExTP0XR7VgFUeAWUewRw6QpwdhtDUcVAbNqANpZ6GfPyBLXF1rmIKcaSy5Vn8vBLYHoLyz3CnCXcZ98BXAJK3AJEniAfvI3hlIFwSp931WGwD3Vl/mnzpSOcsn8HIMS8kS5xCyh2LUIAAkcg82Si84rdZlAwqSOYtA5oVSHbAfIFFX7XtPkyEEzae1M/zllZ4CDzM2PJ5hWAQSlkgUwkNdh96TqW0DE27Sq70C0x5uUJChzCJecr5wJIc9bn4CALZM7IgVkFwBECr8ShPN0Aze6wg5GYhpG4Nm17I9jUXMPYZzNuXF07owjtSFzDSEyz9b3GOVvuFuCVuDkjB7i5Vc2jxieCALDbpZvQzBIt03HDyirGQJuxfXX1jGeBqDbDbZ1r6NQ8BNf4RBTMk588pwBKXAJqfCI4Lj8Cm8JJHYGIatmNNi8vZQy0GRsaSi2rbiCiIpy0P1KXIwDHmQKYr0TLnAJwSwRlbgGywM15gMglIoqB86MKpt6v1DNPUF4dgDUDOD+qIJIHdwBmUg6HMrcwb3omN7eCCFwigU/mwHPE9tpA4aQpgKlBVhxH8IHmSsZCm/Cha5st9r9hUJwfVRBO2isAg5rhPD6Zg0sk80YOz7MDcFhWJKG5RIbMk7xwhZ4ZTs04j9y9ZRVjok247/rGGXb3meFUXrhAZZ6guUTGsiIJ7nkaec8bDVrg4NFYKMHn4GB3hpuiU3QHVQxO8y7cuIKdA+zCigrrBdhgTEN3ULU9EI4npvuzsVCa9wB8SQGIHEGNT0Kxi4csENvvA0IpHWeHU5ZDlsgT3H1dE2NjjnH3dU2WiOFw0pybUMp+/78sEBS7eNT4JIiXOL9ylzpJ1/gElHlEiJz9ZpBBgWP9CYxMu2S5Z8tKxsgc4xNbWyxfjyR0HOtP2H5WpOmFu8wjosZ36SrV8wqA5wjq/RJqCkQ4RM72HAFNN/s/jU67FGut8qJ+WjwKQxZNn0o/VpRZPXCjcQ1H+hLz1uLPBTiOwCFyqCkQUe+X5swDWJAAxrGl1onrqp22m0AUZpxJ+7Ayw9f8+VtbGTNzhD/80AbL1+GkjvZhBcGkYbuVQClwXbUTW2oXljC1IAE0FctYX+lAiYuHkQfl0k8Ekmgbstai2d5aDbcsMnZmGV6nhC1NZZZnbUMpnAjYW65+nJclLh7rKx1oKpYzIwCzzDRQ6xOxqlSCwNkfG9Q+nMKpaX1geY7Dn/3GFsbQLOOhT35ghl/91KDZnNpeAVAIHLCqVEKtT0z3B8hQp3gAaCiUcO9aP2p9IlwiZ2vPsKhioG0ohXMjKUva5gevqYbXwXaBbKHQ48LmFRUTX+sGxbmRFNqGUrYmwOgG4BI51PpE3LvWj4ZCycLdyzaBAPNmuM4v4f51hShy8rb3DesJq9hzMTZhc1JKwRGCr/7v6xlTs4S/v38rOEImVlYKYM/FGHrmqLuZk0Nvuh9YkZPH/esKUeeXFlUzalElfCWeYGu9CzfUu1Hi5pGyMeJvNK7jnd4EekKqRe23tVZjXWM5Y2uGsWl5BTbUF1vGuiek4p3eBEbj9vn+UxpFiZvHDfVubK13QVrkje2ia1jzHMGdzV5sa3RDp/Yly1AA3SEVeztjCEStK9Bf/CbbBTKNv5i2swai5th3h1TbPD+UmuEX2xrduLPZe0mXZ0YEAACVXgGryxxoKZHNfAGb3EKqTvFyewTdQasAGoqd+NKd6xlrM4Qvf3QzqvwOy7PuoIqX2yOXbESdPbvfbNzeUiJjdZkDld6ldaVckgAEjmBNuQO3NXngkTkIvD3RohTAcFzH/q44Lo4plv93340rsbK6iLH3MnFNfRk+vtka9HZxTMH+rjiG47otq79BAYEn8MgcbmvyYE25Y8kh+0tu41HuEXBTgxs3NrjhlTnwNjYEOdwTx56LMUsxJo4Q/NOntzEGXyb++dNbLd6UpGZgz8UYDvfEbXsnngO8MocbG9y4qcGNcs/SexJfFm3LPQI+vsaHLTUuyDaGiw7HdRzojuNof9ISiVhW4MBD929nLF4ivv252+BzTV4oKTrF0f4kDnSbq79dkHmCLTUufHyN77LIf9kCAIAqr4AHNhbio6sKQAix7WDcOabgF8eDCE0LkbijtQK/dcs6xuZF4vc+uAFbpqWchpI6fnE8iM5p5mYuD7yEEHx0VQEe2FiIKu/ld6O/bAEQQlDsEnBnsxcPbPCj0MGDI2bgWi73BJ0CF8YUPH86jKFpOQNfvH0Vtq2uZaxeIG5f14jP3myNsB2KaXj+dBgXxpScFkkgMLnEEaDQweOBDX7c2exFsUtY0EVX1gUw6RkScc/qAty10mvmEosc1ByfjOMqxRsdURzsjs8opfh3916P5koWMXoptNaX4cGPbbY8iykGDnbH8UZHFHE1t3OqGhSyaOb23rXSi3tWF6DSm7nb/oweXR0Chwc2FuKulgKUuQUIXO5PxsNxHa+cjZi3xFNsMVHg8aM/uAMVrKr0nKgr9ePfP3czhCnnOUop9lyM4ZWzEVvsfoFLk7/FNHsy3X6VBAKBjEtaM8y4/WdPRXCsP4GkZpg9B3JoE60skfGxNT7cUOeyXJAkFA2f/u5LM3rbXu2oLfXjsS/eDoc4aVfrBsX+rjiePhHCmRwGuxnU/GyHwGFdpRP3rPZiY5UzK9VJsiIAAFA0irMjKRzqieNgdwLDcQ3RlAHNoBA4Ao7L7mGZI0BziYxPrfNjfYUD0pSVI5ZU8YX/fB1n+8YY8wG01JTg33/3FjintJ1SNANHB5J44lgQ7cOprN7zEAIYBia44ZE5lLgEbKl1YnONCyuKZUhZ6jucNQGMYyCiYn9XHMf6k+gNqwgldcRVA4pGweVgV9hU48TdKwuwvtIqAkU38JdPHMBbp7uuavLfvKYe3/jkFohTLnIUzcDR/iReOBPG4Z7sNiQ3qFlORRIIXCIHn4NHdYGIdZUO3FDnQoU3u9G9WReAeTg10BNS0TaUwrGBBE4MJDES1yDyvGmnk+z2INhU7cTdLQW4tto5I17kX3ecxC/2HL8qyf+ZW9fiD29bbXmmGxTv9CbwQlsYh3uzR36a/gchBKquo9glYE2FA+sqnGgplVGTDrvPupcpFwKglJpt63WKgaiGcyMpHOlLoG1QQXdQgQGzugNPsleKvaVUxm+sKsC2RjdIOqR33I32+sk+fO3x3VcV+b/z2Vuwqal8Yn7Gx2T3hRh+dTo8I+MuY4SD6bJWdQoOQK1fQkuZhI1VTjQVy6jwCJDTfSlIDppTZF0AU4k2vsJEFQNDMQ39YRUjCQOvnougO6QiqhjQdNMOFHnTPLrcTvVkfJulFHV+Cbcu8+DW5e4ZW2v3SBR//tgudI9Er2xPT4kX3/ns9hk91gYiKl4/H8PrHVF0BRVwxBz/yx378TZFqk5NG58n8Ehm8sodTV4UOzlUFogodQvwSJxlh57Onax4mbKusGl/AM8R+Bw8fA4eTcUyKACXRHC0P4m4YpiVhVWKUFJHQjWgpU/KhABc2lYil9hWDVDLAdspcvBKAggBYqqB2fq31RZ78MSXP4JHdrXjpzvfvSLJ//kPbsTv3LRi1uoemmGODSFAqVtERDHHf+pBdSnjz5O0be/l4RTNdkUuicP6SgfuaPLOa/peETvAYsXyekcUbYNJXBxT0BvRoGkUKqXQdQoD6XxkOveSQ2CuXAJHwBNzKy33ClhWKGFliYwN1U6UuPh5XWrnB6P4myf34mIgeEUQf0WlH9/8rW2oKnTN+T2aQTEc1/FubwJnhlPoGFMQiGjpEARz9TabJs4//hwh4ADwPIFICASBoNoroKFQQkuZA7cu8ywoVzdnnMsnAZjeGQpNp9DTyfgST3CsP4WTgSQCMQWjcR0pjc46B0J6dynzmPkK6yodZplsYq5EAmeeNcgCV5e9bX346s/e32eDb/32NmxtuXQfBUrNMTVNFUyMv1mMLIlTg0kMRjWEkjq0WXyiBGZFtiIXj3K3hNZyB9ZVylDSYQw8IRB4suiMrbwTQEwx4JK4nMb5JFUDownTfZrSTHGAYsZezBFzgJ0ih0InD888RVEXal8qmoEXj3TgW8++/b4i/lfv2YSPXNtocW8udSyiioGxhGkSKTqdWRUkPRc8IZDT7swiJw+HmLtIAAqzPet8hXAXLIDpAxJTDAQTOiKKjs4xFZUFApYVyfMS7EqDqht4/t1u/ODXbyOWUvPyHT0OCV/6yHX48LqaBRH/SkFUMdAxmkJ/WEN9oQivxMPv5C1imEvkc+4AlFLo6RDUw90JvDeQQCCqIhDVUe7lsa3Bg5saPBA5mpPDSj6hfSCER3Yex97TPXnxPreva8QDN7dgebnvqpoHSilUg2DPxSh2X4wiENFR7uFR7hGxtsKJTbVO8MSsFj0XR2cIYFwpqk5xfCCJl85G0D6UmrD9DBCAmpn4G6tcuG+tL+u3dfmKeErFsc4hPHXgPA6c6c3pZ9/QUo1PbGnC2oZSOCXhqhz/gYiKJ98L4UhfHMMxHSAEHOjEWbC5VMaHV3hxTYUDIk9m3QVIIBCg0//Hvq44dnVEMRBW0RvRkNSMCbfW+Pln3Nau94v4xDU+bK5x4WpGSjNwPhDG4fMB7DnVhVPdIxn9/a21xdjWWo/rlpdjWZl3oofz1YpDPXE8dTyEzqA6cTYxz4HjHkUzOrnaK6CiQMT2ZR5srXPNWOgndgDVoOgNqxiKavj5iTDOj6QwltAg8dys8ToUZjAbBbC5xomtdW7UF4pYVSpfdSbRbDAoRSiu4uJwFB2DEVwcCqN7KIihYARjsRTiKQ1auryewHNwyQIK3TLKinyoKylAQ2kBlpUXoKHYDa9TXFSxpyvZ5Dk9lELnmIp9XTEc6kmkPXqz300Y1Iz5KnQKWF4s45NrClDqEVBdIE70DZgQwOmhJH7dHsWBzhiCSQMczC57C4GiU/gdPOr9Iv5wcxEq0h8gCyTnExdOKHBKwlV1CLzSF5KURqEaFANhFT88NIrOoIpgUl+wS9UwAAOA38Hh+no37mr2YFWpWeaF9PYP0JfPRvFSexjnR5SpXq0luaKcPMHmOhc+UOdCS4mMco+IXGjAMChePnYRf/eLgwCA//jCnWitZhlgmUDnUASf+rcX8Tcf34IPrWvIWZ8ISs0CXG3DKbzVFcehrjgSS0y1ncrp5cUSPtxcgA+t8ID8469P0yO9CZwfVZHSjYmLoqW8rJGuz+mVOZR7BDSmm+zV+UQ0FErwTevXlKlYj67hCP74J7sQCFmTXL72sS24a2MjY/Bl4Pl3OvDNZw5NfF1T5MG3PrMN9aUFGTNrpnMglNRxcUxBV0hF+3AKF0YVBKIaIiljgmNLoc34RZ/Mc1heJGJjtRNk27cP0ahiQDcAgctMNGZKo5B4Aq+DR7GTQ0OhhFVlDjQVSxAIQYVXgN/JX3ZCjKob+OmuNjz6+ntzfs/v3nYNfndK84xcBFhdKfj+KyfwX2+emH1cb1+L37m5BXwG0l4JAYIJHQMRDRqlODei4HQ6HGYkYSCS1KHoFHIGkmIIzLgnngM8Egey9ptvUX6JirqU2gzDJJzXwcElcvA7ePhkHjcvc2NdpXPiIk0WyKLT3ToGw/ij/3wdo9FLN2ZY11iBv/3E9ajwOSbsStNjwIQwG3rGkvjGU2/hRGdg3u8r9jrx8OdvRX2Jd9GfoRl0orhyVDGQI/3KAAAKtklEQVRwrD+BNztiCKV0BNNJU5GkAULM7MFMz9R4jBPZ+E8Hsh8LlI6L5dKXEh7JzPzxO3iUeETc0eTB+krHgtPu/mv/eXz/xcOLfo0v3bke99/UgrGEDt2gKHELjO3Tx3b3aXz/5WOL+pmv/uYW/MZ1Czc1OQIc7U/i1XNRDEfNA20oqSOqGNDHOxBdbhz8QqmZCwFQTP4x44sugVnf0S1xKHUJ8MocJAFYXerE9bUu1PpnXq6NxBT87ZN7caRjcMnvUuT34TO3X4t7N5SBMr5P4ERfCN957jBOdg8v6ec3N1Xiofu2wuecuah0h1ScTdvy/VEVKY0imDQwHNcQS+eA0Ckr8zhBcrE/52YHuMQ2pKZNpSInj09e48e2RjeqC6wCONY9hi/+6JWMtWf6X5ua8Xu3rUKJ13lVEz8QiuPHr5/GS2+fvezfxRGCR754J1oqrSEZ/REN+zpjeKk9gp6QgqRGQUAg8kA2zO/3jQCmoqZAwC3LPLh3rX9G3u4v3+nCt57Zn5XPvfemVvz2jc0o8shXFfGHIik8+uYZ/OqtUxn/3Q/ddyPuuKbG8uzimIKfHw/i7Z4EooqRN7tvXghgeZGEj6z04o4V3hmH4W8+cwjPv9OR9Xe4aXUtPnPrWrRWeq9o4h/tCeO/d72HfVkO5Ltv60r80V3WdqojcQ1PnwjhjY4ogkmDCQAAVpfJuGtlAbbUOOGRJ+8JkqqOrz2xD/vP9OV2Jyr24P6b1+DW1hoUOK6MQ/JYXMMbJ3vwxO4T6B3NXc7zzatr8dB9H4DIkQnX80BExctno3ijI4pAVLt6BcARc+W/Z7UPH6hzWUpgjMYU/Pljb6Ktd8TWwdnSXIm7NzbiuuUV8Lmk9xXpg3EF75wfwLNvd+DtcwO2vceauhL8ywPb4Z2ymPSFVbx2PmpbucW8EMCKYgkfXVWAW5d7LP74vlAS//c/dqJ/LL+qMzRXFeFjm5djXWMZKgvdeRdrpOoG+sdiOHphEL84eB7n+kfz5t1qSwrwg9+/HUXuyUVkKKbh5bMRPH86jHDKuLoE0FIq4+6VXtwyjfwXRhL40g93YCyeQr5jRVUR7lhbh/UNJagp8sLrFDNyK7oQ6IaBSEJF72gUb50bxJsne3C+fySvx6vY48SPvnAnqnyTzoZgUsezp0J49VwUIzbtBDkXwPIiCb+5ugAfqHdbzJ6u0Th+/wcvI/w+IP+ck+x1Yl1DGVbXFKGhtAAlBQ74nBKcsghZ4MBzZt2buUIxKKXQDQrdMJDSDCQUDaF4CsPhJDoGwzjdM4pjnYMYjSTel+Pjczvwky99GJUFsmUneO50GG902COCnAqgyivgY2t82Nbgthx4e4MJ/MEPXsFoNAGGKxvFXhce/dKHUDLF7dwZVPByewQvn43kvP9AzgzZCo+A7cs8+OAKLzwyP1EbZjCi4I8eeY2R/yrBSCSOL/zHToSTkx6ger+EO5u9uL7WBYdArjwBuCUOWxvcuH+df8LPTwhBXNHx54+9kXcHXobsomc4gj959A2kppToq/NLeGBjEVaWyMhhNZXcCGBbgxt3LPfMuOH96yf24Vw/q9F/NaKtZwR//fi+ibMPAJR5TBN5ebF85QigtUzGzY1u1Bda/ejfeu4IDrb3MSZcxdh/phcP7zhmcQpcV+3EjfXuJXd+zysBFDl5fGyND80lVkU/vqcNvzzYzhjAgMf3nMavDk+GuhBCcMsyN25d5nl/C6DMLeDuFi+uKXfAOcWoe/14Fx7ecZTNPMME/vlXh3DwbP/kwukScEO9Cx9s8iDbpUSzIgCZJ9hQ5cCH0h6fcfSHU3jwf95iM84wA3/22G4MRidLTjYUSrhrZQGWF0lZbaOVFQG0ljtwY70bRa5JO45Sigef2JexeH6GKwsGpfjrx/dOcIUjBM0lEm5Z7kFZFjP3Mi4AgQO21ruwocqaaPLDnadwsmuQzTTDnDjZGcBPd5+xHIrvaPKipVR+fwhA5IC7WwqwtsJhcXm+1d6Pn+06zmaY4ZL48cvvTpSVJMRMmb1luQebqp35LQCOAPWFEm5b7rGkMwZjKTz45H42swwLxpcfexPxKe2ZNlQ6sbXBDZ8j830pMiaAYhePrfVuNBZKEx0HAeAff3kob+vpM+QnIgkF3/n1ZGUKkSdYU+7AlhpXxvOHMyaAZYUSbml0T1Z9IAR72wex+3Qvm1GGReP5Q2fwzsXJEO9St4DblntmVBfMCwEsK5KwpdaFcu9kFeOkouGhJ/ewmWRYMv7yZ7ugG6YpJPEEjUUSbmpwo9DJ55cArq1yYtO0/gA/fv00M30YLgvRpIpHd0+Wa3FLHO5o8qAqg2ESlyUAAqDUzaO13GGpstbWH8aTe06yGWS4bPzk1XfROxY3yUoImopltJQ54MxQ2PRlCUASCG5b7kXDtCpu33vxbTZzDBnDv71g5dPmaieuzZBb9LIE4BY5bG90o3xKj7A3zgzj3Qvswoshc9jX1oddJ7snvl5b6Zxx0ZpzAXhlDhurnRbTx6AU3/4V8/kzZB7fefFdS4f5hkIJ15Q7LjtYbskCKPcIuLnBbanZ/tJ7vRgOx9lsMWQcgVAcu9omy7VXeUXc1OCekWSVMwFUeUWsr3JOpDiquoF/+dUBNlMMWcPf/3wf9HQNfb+Tx4YqJ7wyl3sB1PtFXFPhsNTx3PleF5KKxmaJIWtIKCrenHKxWujksaXWBZ+Dy60AVpbIWF02GaGnGRQPv8SSXBiyj394+uDELiDxBJtrXChxCbkTAEeAFSUy6vyTOb4dg2GMxZJsdhiyvwukVOxrN2udCpxZXHl6L4msCYAj5um73i9azJ9vPs1sf4bc4eEX3wFgxpt5ZR7LiiQUu/jsC0DkCNZVOCyZXoFwCmf6WGkThtyhZzSKo93Bia+XF0lYXiRlXwCEAOsrnRa1zdeilIEhW/jpa5Ph0k0l8pJrCS1KAMUuAfWFIhyC+WOKZuC5w+fZbDDkHIfO9mMspgAA/A4edT5xSRXlFvwjHonDylIZ3ilVHva3D7CZYLANv3z74sR/l3kErCiRF30zvGAB+J08WqZ9wINP7mWzwGAbfvLquxNVRnwOHi2l8qIbri9YAD7Z3AHGP2AoHIemG2wWGGyDQSkuBEITZtDKEnnRNYQWtQPU+8WJ2IsXj1xkM8BgO35+4BwAM1mmvlCCY5EHAcElXloyEk9Q65Mgpw+/BqX48avM+8NgP54/fA5/8pENcIo8SlwCGvwiVN3stLOQBHrhj28omfcbKACXyFlu27qGI2zkGfIGxy4M4vrmSjgEggeuLcJoXENKowsyh4Rrq12X/CaeMy/BxrHj3Qts1BnyBq8c68T1zZXgiFmdpM4npneASyuABAKBRRXrpJRi69f+h406Q15hz9/du6TcgEVfHYykLx8YGPIJ7YGlmeWLFsDB80NstBnyDrtO9eZGAE+8eYKNNkPe4eV3zmZfALpB0REIstFmyDsMhuJILCEjcVECYEkvDPmMU32h7ArgMLP/GfIYe9r6syuA59/pYKPMkLd4q60newKglOJoBwt/ZshfdA8FoS4yQHPBAoinWMkThvzHaCSRHQEEQqziG0P+o3ORcWoLFkBb3ygbXYa8R1vPaHYEcIClPzK8D3C4YzA7AnjjRBcbXYa8x5FFOmr+PxPxlnqYyeiPAAAAAElFTkSuQmCC");
        }
        startActivityForResult(crea, 0);

    }
}
